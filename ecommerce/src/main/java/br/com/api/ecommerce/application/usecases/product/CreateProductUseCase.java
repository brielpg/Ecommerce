package br.com.api.ecommerce.application.usecases.product;

import br.com.api.ecommerce.application.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.application.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.application.mappers.ProductMapper;
import br.com.api.ecommerce.application.repositories.ProductRepository;
import br.com.api.ecommerce.application.usecases.category.GetCategoriesByIdsUseCase;
import br.com.api.ecommerce.domain.exceptions.ConflictException;
import br.com.api.ecommerce.domain.models.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CreateProductUseCase {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final GetCategoriesByIdsUseCase getCategoriesByIdsUseCase;

    public ProductDtoList execute(ProductDtoCreate dto, MultipartFile imageFile) {
        if (repository.existsByName(dto.name()))
            throw new ConflictException("exception.product.name.already.exists");

        Product product = mapper.toEntity(dto);

        if (!dto.categories().isEmpty())
            product.setCategories(getCategoriesByIdsUseCase.execute(dto.categories()));

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                product.setImage(imageFile.getBytes());
            } catch (IOException e) {
                log.error("Erro ao processar imagem do produto {}", dto.name(), e);
            }
        }

        repository.save(product);
        log.info("Produto criado com sucesso, ID: {}", product.getId());
        return mapper.toDto(product);
    }
}
