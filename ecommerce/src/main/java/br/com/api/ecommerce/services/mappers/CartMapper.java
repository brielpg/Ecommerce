package br.com.api.ecommerce.services.mappers;

import br.com.api.ecommerce.models.Cart;
import br.com.api.ecommerce.models.dtos.Cart.CartDtoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    @Autowired
    private ItemMapper itemMapper;

    public CartDtoList toDto(Cart entity) {
        if (entity == null) return null;
        return new CartDtoList(
                entity.getId(),
                itemMapper.toDtoList(entity.getItems()),
                entity.getTotalPrice()
        );
    }
}
