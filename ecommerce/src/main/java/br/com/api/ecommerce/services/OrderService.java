package br.com.api.ecommerce.services;

import br.com.api.ecommerce.core.models.*;
import br.com.api.ecommerce.core.exceptions.NotFoundException;
import br.com.api.ecommerce.core.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.core.models.enums.OrderStatus;
import br.com.api.ecommerce.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final UserService userService;
    private final AddressService addressService;
    private final ItemService itemService;

    @Transactional(readOnly = true)
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public Order getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.order.not.found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public Page<Order> getAllByUser(UUID userId, Pageable pageable) {
        return repository.findOrdersByUserId(userId, pageable);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public Order create(OrderDtoCreate dto) {
        User user = userService.getById(dto.userId());

        addressService.existsByIdAndUser(dto.addressId(), dto.userId());
        Address deliveryAddress = addressService.getById(dto.addressId());

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        List<Item> items = itemService.createListOfItems(dto.items(), order);
        order.setItems(items);

        order.calculateTotalPrice();

        items.forEach(item -> {
            Product product = item.getProduct();
            log.debug("Baixando estoque: Produto ID {}, Quantidade {}", product.getId(), item.getQuantity());
            product.setStock(product.getStock() - item.getQuantity());
        });

        log.info("Pedido criado com sucesso. OrderId: {}, Total: R$ {}", order.getId(), order.getTotalPrice());
        return repository.save(order);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndProductIdAndStatus(UUID userId, UUID productId, OrderStatus status) {
        return repository.existsByUserIdAndProductIdAndStatus(userId, productId, status);
    }
}
