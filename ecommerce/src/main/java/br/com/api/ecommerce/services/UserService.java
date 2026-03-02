package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.models.enums.EventTypes;
import br.com.api.ecommerce.repositories.UserRepository;
import br.com.api.ecommerce.services.mappers.AddressMapper;
import br.com.api.ecommerce.services.mappers.ProductMapper;
import br.com.api.ecommerce.services.mappers.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailProducer emailProducer;

    @Transactional
    public UserDtoList create(UserDtoCreate dto){
        this.existsByEmail(dto.email());

        User user = mapper.toEntity(dto);
        user.setAddresses(addressMapper.toEntityList(dto.addresses(), user));

        user.setPassword(authorizationService.encodePassword(dto.password()));
        user.setCart(cartService.create(user));

        this.emailSending(EventTypes.USER_WELCOME, user);

        this.save(user);
        log.info("Usuário criado com sucesso. ID: {}", user.getId());
        return mapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDtoList> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(user -> mapper.toDto(user));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public User getById(UUID id) {
        if (authorizationService.validateAdminUser()) {
            Optional<User> user = repository.findById(id);
            if (user.isPresent()) return user.get();
        }

        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.user.not.found"));
    }

    @Transactional
    @PreAuthorize("#dto.id() == authentication.principal.id or hasRole('ADMIN')")
    public UserDtoList update(UserDtoUpdate dto) {
        User user = this.getById(dto.id());

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword()))
            throw new BadRequestException("exception.user.current.password.invalid");

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            this.existsByEmail(dto.email());
            user.setEmail(dto.email());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());

        if (dto.newPassword() != null && !dto.newPassword().isBlank()) {
            String encodedNewPassword = authorizationService.encodePassword(dto.newPassword());
            user.setPassword(encodedNewPassword);
        }

        if (dto.addresses() != null) {
            user.getAddresses().clear();
            List<Address> newAddresses = addressService.create(dto.addresses(), user);
            user.getAddresses().addAll(newAddresses);
        }

        this.save(user);
        return mapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void restore(UUID id) {
        User user = this.getById(id);

        user.setActive(true);
        this.emailSending(EventTypes.USER_REACTIVATED, user);
        this.save(user);
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void delete(UUID id) {
        User user = this.getById(id);

        user.setActive(false);
        this.emailSending(EventTypes.USER_DEACTIVATED, user);
        this.save(user);

        log.info("Usuário desativado com sucesso: {}", id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public List<ProductDtoList> getFavorites(UUID id) {
        List<Product> favorites = repository.getFavorites(id);

        return favorites.stream().map(p -> productMapper.toDto(p)).toList();
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void addFavorite(UUID id, UUID productId) {
        repository.addFavorite(id, productId);
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void removeFavorite(UUID id, UUID productId) {
        repository.removeFavorite(id, productId);
    }

    @Transactional
    public void removeProductFromAllFavorites(UUID productId) {
        repository.removeProductFromAllFavorites(productId);
    }

    @Transactional(readOnly = true)
    private void existsByEmail(String email){
        if (repository.existsByEmail(email))
            throw new ConflictException("exception.user.email.already.registered");
    }

    @Transactional
    private void save(User user) {
        repository.save(user);
    }

    private void emailSending(EventTypes eventType, User user) {
        emailProducer.publishEvent(eventType, Map.of(
                "emailTo", user.getEmail(),
                "userName", user.getName(),
                "userPhone", user.getPhone()
        ));
    }
}
