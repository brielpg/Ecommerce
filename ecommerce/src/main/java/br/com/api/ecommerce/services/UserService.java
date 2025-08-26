package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
import br.com.api.ecommerce.models.dtos.User.UserDtoList;
import br.com.api.ecommerce.models.dtos.User.UserDtoUpdate;
import br.com.api.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Transactional
    public User create(UserDtoCreate dto){
        this.existsByEmail(dto.email());

        User user = dtoToEntity(dto);
        Cart cart = cartService.create(user);
        user.setCart(cart);

        return repository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<UserDtoList> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public User getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.user.not.found"));
    }

    @Transactional
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
    public void delete(UUID id) {
        User user = this.getById(id);

        user.setActive(false);
        repository.save(user);
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
