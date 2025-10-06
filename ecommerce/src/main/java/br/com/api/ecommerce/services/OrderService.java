package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.*;
import br.com.api.ecommerce.models.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ItemService itemService;

    @Transactional(readOnly = true)
    public Order getById(UUID id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.order.not.found"));

        authorizationService.validateCurrentUser(order.getUser().getId());

        return order;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public Page<Order> getAllByUser(UUID userId, Pageable pageable) {
        return repository.findOrdersByUserId(userId, pageable);
    }

    @Transactional
    public Order create(OrderDtoCreate dto) {
        authorizationService.validateCurrentUser(dto.userId());

        User user = userService.getById(dto.userId());

        addressService.existsByIdAndUser(dto.addressId(), dto.userId());
        Address deliveryAddress = addressService.getById(dto.addressId());

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);
        if (dto.couponCode() != null)
            order.setCoupon(couponService.getByCode(dto.couponCode()));

        List<Item> items = itemService.createListOfItems(dto.items(), order);

        BigDecimal subtotal = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setSubtotal(subtotal);

        BigDecimal totalPrice = (order.getCoupon() != null)
                ? couponService.calculateDiscount(order, subtotal)
                : subtotal;

        order.setTotalPrice(totalPrice);

        items.forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
        });

        return repository.save(order);
    }
}
