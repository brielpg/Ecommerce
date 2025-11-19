package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthorizationService authorizationService;

    @Transactional
    public User create(UserDtoCreate dto){
        this.existsByEmail(dto.email());

        User user = dtoToEntity(dto);
        Cart cart = cartService.create(user);
        user.setCart(cart);
        user.setPassword(authorizationService.encodePassword(dto.password()));

        return repository.save(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDtoList> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public User getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.user.not.found"));
    }

    @Transactional
    @PreAuthorize("#dto.id() == authentication.principal.id or hasRole('ADMIN')")
    public User update(UserDtoUpdate dto) {
        User user = this.getById(dto.id());

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            this.existsByEmail(dto.email());
            user.setEmail(dto.email());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());

        return repository.save(user);
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void restore(UUID id) {
        User user = this.getById(id);

        user.setActive(true);
        repository.save(user);
    }

    @Transactional
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void delete(UUID id) {
        User user = this.getById(id);

        user.setActive(false);
        repository.save(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public List<ProductDtoList> getFavorites(UUID id) {
        List<Product> favorites = repository.getFavorites(id);

        return favorites.stream().map(p -> productService.entityToDto(p)).toList();
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

    private User dtoToEntity(UserDtoCreate dto){
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setBirthDate(dto.birthDate());
        user.setAddresses(addressService.create(dto.addresses(), user));

        return user;
    }
}
