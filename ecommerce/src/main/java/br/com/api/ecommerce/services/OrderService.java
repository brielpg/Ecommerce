package br.com.api.ecommerce.services;

import br.com.api.ecommerce.models.*;
import br.com.api.ecommerce.models.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.models.enums.OrderStatus;
import br.com.api.ecommerce.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
                .orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Page<Order> getAllByUser(UUID userId, Pageable pageable) {
        return repository.findAllByUser_Id(userId, pageable);
    }

    @Transactional
    public Order create(OrderDtoCreate dto) {
        User user = userService.getById(dto.userId());

        Order order = new Order();
        List<Item> items = itemService.createListOfItems(dto.items(), order);

        order.setUser(user);
        order.setItems(items);

        BigDecimal total = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);

        Address deliveryAddress = addressService.getById(dto.addressId());
        if (user.getAddresses().stream().noneMatch(addr -> addr.getId().equals(deliveryAddress.getId()))) throw new RuntimeException();
        order.setDeliveryAddress(deliveryAddress);

        items.forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
        });

        repository.save(order);
        return order;
    }
}
