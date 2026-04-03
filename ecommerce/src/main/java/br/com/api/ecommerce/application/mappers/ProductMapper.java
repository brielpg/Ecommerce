package br.com.api.ecommerce.application.mappers;

import br.com.api.ecommerce.domain.models.Product;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDtoList toDto(Product entity) {
        if (entity == null) return null;
        return new ProductDtoList(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getRating(),
                entity.getPurchaseCount(),
                entity.getActive(),
                entity.getCreatedAt()
        );
    }

    public Product toEntity(ProductDtoCreate dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        return product;
    }
}
