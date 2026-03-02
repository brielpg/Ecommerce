package br.com.api.ecommerce.services.mappers;

import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.dtos.Item.ItemDtoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ItemMapper {

    @Autowired
    private ProductMapper productMapper;

    public ItemDtoList toDto(Item entity) {
        if (entity == null) return null;
        return new ItemDtoList(
                entity.getId(),
                productMapper.toDto(entity.getProduct()),
                entity.getQuantity(),
                entity.getSubTotal()
        );
    }

    public List<ItemDtoList> toDtoList(List<Item> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toDto).toList();
    }
}
