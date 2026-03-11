package br.com.api.ecommerce.core.mappers;

import br.com.api.ecommerce.core.models.Item;
import br.com.api.ecommerce.core.dtos.Item.ItemDtoList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final ProductMapper productMapper;

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
