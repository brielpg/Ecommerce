package br.com.api.ecommerce.application.dtos.Cart;

import br.com.api.ecommerce.application.dtos.Item.ItemDtoList;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDtoList(
        UUID id,
        List<ItemDtoList> items,
        BigDecimal totalPrice
) {
}
