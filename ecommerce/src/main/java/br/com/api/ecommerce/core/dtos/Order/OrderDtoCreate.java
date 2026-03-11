package br.com.api.ecommerce.core.dtos.Order;

import br.com.api.ecommerce.core.dtos.Item.DtoItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderDtoCreate(
        @NotNull(message = "{dto.order.userid.notnull}")
        UUID userId,
        @NotNull(message = "{dto.order.addressid.notnull}")
        UUID addressId,
        @NotEmpty(message = "{dto.order.items.notempty}")
        List<@Valid DtoItemRequest> items
) {
}
