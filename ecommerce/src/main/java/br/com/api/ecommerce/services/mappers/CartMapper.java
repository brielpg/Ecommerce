package br.com.api.ecommerce.services.mappers;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final ItemMapper itemMapper;

    public CartDtoList toDto(Cart entity) {
        if (entity == null) return null;
        return new CartDtoList(
                entity.getId(),
                itemMapper.toDtoList(entity.getItems()),
                entity.getTotalPrice()
        );
    }
}
