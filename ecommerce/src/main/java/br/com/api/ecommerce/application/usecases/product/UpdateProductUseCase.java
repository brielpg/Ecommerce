package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.application.usecases.item.UpdateItemsPriceUseCase;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final UpdateItemsPriceUseCase updateItemsPriceUseCase;

    public ProductDtoList execute(ProductDtoUpdate dto, MultipartFile imageFile) {
        Product product = getProductByIdUseCase.execute(dto.id());

        if (dto.name() != null && repository.existsByName(dto.name()))
            throw new ConflictException("exception.product.name.already.exists");

        if (dto.name() != null) product.setName(dto.name());
        if (dto.description() != null) product.setDescription(dto.description());
        if (dto.stock() != null) product.setStock(dto.stock());

        if (dto.price() != null && !dto.price().equals(product.getPrice())) {
            product.setPrice(dto.price());
            updateItemsPriceUseCase.execute(product);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImage(imageFile.getBytes());
            } catch (IOException ignored) {}
        }

        repository.save(product);
        return mapper.toDto(product);
    }
}
