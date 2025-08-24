package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.Address;
import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.User;
import br.com.api.ecommerce.models.dtos.User.UserDtoCreate;
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

//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private OrderService orderService;

    @Transactional
    public User create(UserDtoCreate dto){
        if (repository.existsByEmail(dto.email())) throw new RuntimeException();

        User user = dtoToEntity(dto);
//        Cart cart = cartService.create(user);
//        user.setCart(cart);

        repository.save(user);
        return user;
    }

    @Transactional
    public void delete(UUID id) {
        User user = repository.findByIdAndActiveTrue(id)
                .orElseThrow(RuntimeException::new);

        user.setActive(false);
        repository.save(user);
    }

    @Transactional
    public User update(UserDtoUpdate dto) {
        User user = repository.findByIdAndActiveTrue(dto.id())
                .orElseThrow(RuntimeException::new);

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.email())) {
                throw new RuntimeException();
            }
            user.setEmail(dto.email());
        }

        if (dto.name() != null) user.setName(dto.name());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());

        repository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Page<User> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
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

//    @Transactional(readOnly = true)
//    public Page<Order> getOrdersByUser(UUID userId, Pageable pageable) {
//        if (!repository.existsByIdAndActiveTrue(userId))
//            throw new RuntimeException();
//
//        return orderService.findByUserId(userId, pageable);
//    }
//
//    @Transactional(readOnly = true)
//    public Cart getCartByUser(UUID userId) {
//        if (!repository.existsByIdAndActiveTrue(userId))
//            throw new RuntimeException();
//
//        return cartService.findByUserId(userId);
//    }
}
