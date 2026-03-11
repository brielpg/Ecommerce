package br.com.api.ecommerce.core.dtos.Item;

import br.com.api.ecommerce.core.dtos.Product.ProductDtoList;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemDtoList(
        UUID id,
        ProductDtoList product,
        Integer quantity,
        BigDecimal subTotal
) {
}
