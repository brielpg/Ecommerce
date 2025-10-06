package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.exceptions.ConflictException;
import br.com.api.ecommerce.exceptions.NotFoundException;
import br.com.api.ecommerce.models.Coupon;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.Product;
import br.com.api.ecommerce.models.dtos.Coupon.CouponDtoCreate;
import br.com.api.ecommerce.models.dtos.Coupon.CouponDtoUpdate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoCreate;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoList;
import br.com.api.ecommerce.models.dtos.Product.ProductDtoUpdate;
import br.com.api.ecommerce.models.enums.DiscountType;
import br.com.api.ecommerce.repositories.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon create(CouponDtoCreate dto){
        this.existsByCode(dto.code());
        this.validDates(dto.validFrom(), dto.validUntil());

        Coupon coupon = dtoToEntity(dto);

        if (dto.productIds() != null && !dto.productIds().isEmpty()) {
            coupon.setProducts(productService.findAllById(dto.productIds()));
        }
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            coupon.setCategories(categoryService.findAllById(dto.categoryIds()));
        }

        this.save(coupon);
        return coupon;
    }

    @Transactional(readOnly = true)
    public Page<Coupon> getAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Coupon getById(UUID id) {
        return repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("exception.coupon.not.found"));
    }

    @Transactional(readOnly = true)
    public Coupon getByCode(String code) {
        return repository.findByCodeAndActiveTrue(code)
                .orElseThrow(() -> new NotFoundException("exception.coupon.not.found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon update(CouponDtoUpdate dto) {
        Coupon coupon = this.getById(dto.id());
        this.existsByCode(dto.code());
        this.validDates(dto.validFrom(), dto.validUntil());

        if (dto.code() != null) coupon.setCode(dto.code());
        if (dto.description() != null) coupon.setDescription(dto.description());
        if (dto.discountType() != null) coupon.setDiscountType(dto.discountType());
        if (dto.value() != null) coupon.setValue(dto.value());
        if (dto.validFrom() != null) coupon.setValidFrom(dto.validFrom());
        if (dto.validUntil() != null) coupon.setValidUntil(dto.validUntil());
        if (dto.maxUses() != null) coupon.setMaxUses(dto.maxUses());

        if (dto.productIds() != null && !dto.productIds().isEmpty()) {
            coupon.setProducts(productService.findAllById(dto.productIds()));
        }
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            coupon.setCategories(categoryService.findAllById(dto.categoryIds()));
        }

        this.save(coupon);
        return coupon;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        Coupon coupon = this.getById(id);

        coupon.setActive(false);
        this.save(coupon);
    }

    @Transactional
    private void save(Coupon coupon){
        repository.save(coupon);
    }

    @Transactional(readOnly = true)
    private void existsByCode(String code){
        if (repository.existsByCode(code))
            throw new ConflictException("exception.coupon.code.already.exists");
    }

    private void validDates(LocalDate validFrom, LocalDate validUntil){
        long daysBetween = ChronoUnit.DAYS.between(validFrom, validUntil);

        if (daysBetween < 1)
            throw new ConflictException("exception.coupon.invalid.dates");
    }

    public BigDecimal calculateDiscount(Order order, BigDecimal subtotal) {
        if (!order.getCoupon().isValid())
            throw new BadRequestException("exception.coupon.invalid.or.expired");

        Coupon coupon = order.getCoupon();

        // Filter only elegible items (product or category)
        List<Item> eligibleItems = order.getItems().stream()
                .filter(item ->
                        coupon.getProducts().contains(item.getProduct()) ||
                        coupon.getCategories().stream().anyMatch(c -> item.getProduct().getCategories().contains(c)))
                .toList();

        BigDecimal eligibleTotal = eligibleItems.stream()
                .map(Item::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = BigDecimal.ZERO;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE)
            discount = eligibleTotal.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
        else
            discount = coupon.getValue().min(eligibleTotal);

        return subtotal.subtract(discount);
    }

    private Coupon dtoToEntity(CouponDtoCreate dto){
        Coupon coupon = new Coupon();

        coupon.setCode(dto.code());
        coupon.setDiscountType(dto.discountType());
        coupon.setValue(dto.value());
        coupon.setValidFrom(dto.validFrom());
        coupon.setValidUntil(dto.validUntil());

        if (dto.description() != null)
            coupon.setDescription(dto.description());

        if (dto.maxUses() != null)
            coupon.setMaxUses(dto.maxUses());

        return coupon;
    }
}
