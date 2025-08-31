package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.*;
import br.com.api.ecommerce.models.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ItemService itemService;

    @Transactional(readOnly = true)
    public Order getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.order.not.found"));
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllByUser(UUID userId, Pageable pageable) {
        return repository.findOrdersByUserId(userId, pageable);
    }

    @Transactional
    public Order create(OrderDtoCreate dto) {
        User user = userService.getById(dto.userId());

        addressService.existsByIdAndUser(dto.addressId(), dto.userId());
        Address deliveryAddress = addressService.getById(dto.addressId());

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        List<Item> items = itemService.createListOfItems(dto.items(), order);

        BigDecimal total = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotalPrice(total);

        items.forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
        });

        return repository.save(order);
    }
}
