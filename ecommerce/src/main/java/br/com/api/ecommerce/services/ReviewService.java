package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.exceptions.AccessDeniedException;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.Review;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoCreate;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoList;
import br.com.api.ecommerce.models.dtos.Review.ReviewDtoUpdate;
import br.com.api.ecommerce.models.enums.OrderStatus;
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
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Transactional
    @PreAuthorize("#dto.userId() == authentication.principal.id or hasRole('ADMIN')")
    public ReviewDtoList create(ReviewDtoCreate dto){
        this.existsByUserIdAndProductId(dto.userId(), dto.productId());

        if (!orderService.existsByUserIdAndProductIdAndStatus(dto.userId(), dto.productId(), OrderStatus.DELIVERED)) {
            throw new AccessDeniedException("exception.review.purchase.required");
        }

        Review review = dtoToEntity(dto);

        this.save(review);

        productService.updateProductRating(dto.productId());

        return entityToDto(review);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("permitAll()")
    public Page<ReviewDtoList> getAllByProduct(UUID productId, Pageable pageable) {
        return repository.findAllByProductId(pageable, productId).map(this::entityToDto);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("permitAll()")
    private Review getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("exception.review.not.found"));
    }

    @Transactional
    @PreAuthorize("@reviewService.getUserIdFromReview(#dto.id()) == authentication.principal.id or hasRole('ADMIN')")
    public ReviewDtoList update(ReviewDtoUpdate dto) {
        Review review = this.getById(dto.id());

        if (dto.rating() != null) review.setRating(dto.rating());
        if (dto.review() != null) review.setReview(dto.review());

        this.save(review);

        productService.updateProductRating(review.getProduct().getId());

        return entityToDto(review);
    }

    @Transactional
    @PreAuthorize("@reviewService.getUserIdFromReview(#id) == authentication.principal.id or hasRole('ADMIN')")
    public void delete(UUID id) {
        Review review = this.getById(id);
        Product product = review.getProduct();

        product.getReviews().remove(review);
        repository.delete(review);

        productService.updateProductRating(review.getProduct().getId());
    }

    @Transactional(readOnly = true)
    private void existsByUserIdAndProductId(UUID userId, UUID productId){
        if (repository.existsByUserIdAndProductId(userId, productId))
            throw new ConflictException("exception.review.already.exists");
    }

    @Transactional(readOnly = true)
    public UUID getUserIdFromReview(UUID reviewId) {
        return this.getById(reviewId).getUser().getId();
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
