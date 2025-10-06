package br.com.api.ecommerce.services;

import br.com.api.ecommerce.exceptions.BadRequestException;
import br.com.api.ecommerce.models.Coupon;
import br.com.api.ecommerce.models.Item;
import br.com.api.ecommerce.models.Order;
import br.com.api.ecommerce.models.enums.DiscountType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CouponService {

    public BigDecimal calculateDiscount(Order order, BigDecimal subtotal) {
        if (!order.getCoupon().isValid())
            throw new BadRequestException("exception.invalid.discount.coupon");

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
}
