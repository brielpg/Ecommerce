package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.Review;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoUpdate;
import br.com.api.ecommerce.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // TODO adjust PreAuthorizes to support 'authentication.principal.id'

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ReviewDtoList create(ReviewDtoCreate dto){
        this.existsByUserIdAndProductId(dto.userId(), dto.productId());
        // TODO implement purchase required validation before create new Review

        Review review = dtoToEntity(dto);

        this.save(review);

        productService.updateProductRating(dto.productId());

        return entityToDto(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDtoList> getAllByProduct(UUID productId, Pageable pageable) {
        return repository.findAllByProductId(pageable, productId).map(this::entityToDto);
    }

    @Transactional(readOnly = true)
    private Review getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.review.not.found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ReviewDtoList update(ReviewDtoUpdate dto) {
        Review review = this.getById(dto.id());

        if (dto.rating() != null) review.setRating(dto.rating());
        if (dto.review() != null) review.setReview(dto.review());

        this.save(review);

        productService.updateProductRating(review.getProduct().getId());

        return entityToDto(review);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        // TODO fix method error
        Review review = this.getById(id);

        repository.delete(review);

        productService.updateProductRating(review.getProduct().getId());
    }

    @Transactional(readOnly = true)
    private void existsByUserIdAndProductId(UUID userId, UUID productId){
        if (repository.existsByUserIdAndProductId(userId, productId))
            throw new ConflictException("exception.review.already.exists");
    }

    @Transactional
    private void save(Review review){
        repository.save(review);
    }

    public Review dtoToEntity(ReviewDtoCreate dto) {
        Review review = new Review();
        review.setRating(dto.rating());
        review.setReview(dto.review());
        review.setUser(userService.getById(dto.userId()));
        review.setProduct(productService.getById(dto.productId()));

        return review;
    }

    public ReviewDtoList entityToDto(Review entity) {
        return new ReviewDtoList(
                entity.getId(),
                entity.getUser().getId(),
                entity.getProduct().getId(),
                entity.getRating(),
                entity.getReview(),
                entity.getTimestamp()
        );
    }
}
