package br.com.api.ecommerce.core.dtos.Cart;

import br.com.api.ecommerce.core.dtos.Item.ItemDtoList;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDtoList(
        UUID id,
        List<ItemDtoList> items,
        BigDecimal totalPrice
) {
}
