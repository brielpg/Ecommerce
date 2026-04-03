package br.com.api.ecommerce.application.mappers;

import br.com.api.ecommerce.domain.models.Category;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoCreate;
import br.com.api.ecommerce.application.dtos.Category.CategoryDtoList;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDtoList toDto(Category entity) {
        if (entity == null) return null;
        return new CategoryDtoList(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getActive(),
                entity.getCreatedAt()
        );
    }

    public Category toEntity(CategoryDtoCreate dto) {
        Category category = new Category();
        category.setName(dto.name());
        category.setDescription(dto.description());

        return category;
    }
}
