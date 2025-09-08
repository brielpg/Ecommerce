package br.com.api.ecommerce.models.dtos.Item;

import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemDtoList(
        UUID id,
        ProductDtoList product,
        Integer quantity,
        BigDecimal subTotal
) {
}
