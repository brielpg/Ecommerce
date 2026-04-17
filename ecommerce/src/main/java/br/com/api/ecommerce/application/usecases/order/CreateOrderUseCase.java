package br.com.api.ecommerce.application.usecases.order;

import br.com.api.ecommerce.application.dtos.Order.OrderDtoCreate;
import br.com.api.ecommerce.application.repositories.OrderRepository;
import br.com.api.ecommerce.application.usecases.address.GetAddressByIdUseCase;
import br.com.api.ecommerce.application.usecases.address.ValidateAddressOwnershipUseCase;
import br.com.api.ecommerce.application.usecases.item.CreateOrderItemsUseCase;
import br.com.api.ecommerce.application.usecases.user.GetUserByIdUseCase;
import br.com.api.ecommerce.domain.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase {
    private final OrderRepository repository;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetAddressByIdUseCase getAddressByIdUseCase;
    private final ValidateAddressOwnershipUseCase validateAddressOwnershipUseCase;
    private final CreateOrderItemsUseCase createOrderItemsUseCase;

    public Order execute(OrderDtoCreate dto) {
        User user = getUserByIdUseCase.execute(dto.userId());

        validateAddressOwnershipUseCase.execute(dto.addressId(), dto.userId());
        Address deliveryAddress = getAddressByIdUseCase.execute(dto.addressId());

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(deliveryAddress);

        List<Item> items = createOrderItemsUseCase.execute(dto.items(), order);
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
}
