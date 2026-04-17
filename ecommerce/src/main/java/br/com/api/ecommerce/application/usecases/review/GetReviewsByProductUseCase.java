package br.com.api.ecommerce.application.usecases.review;

import br.com.api.ecommerce.application.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.application.mappers.ReviewMapper;
import br.com.api.ecommerce.application.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RequiredArgsConstructor
public class GetReviewsByProductUseCase {
    private final ReviewRepository repository;
    private final ReviewMapper mapper;

    public Page<ReviewDtoList> execute(UUID productId, Pageable pageable) {
        return repository.findAllByProductId(pageable, productId)
                .map(mapper::toDto);
    }
}
