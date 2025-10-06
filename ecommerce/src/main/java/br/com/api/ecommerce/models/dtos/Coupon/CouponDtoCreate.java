package br.com.api.ecommerce.models.dtos.Coupon;

import br.com.api.ecommerce.models.enums.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CouponDtoCreate(
        @NotBlank(message = "{dto.coupon.code.notblank}")
        String code,
        String description,
        @NotNull(message = "{dto.coupon.discount.type.notnull}")
        DiscountType discountType,
        @NotNull(message = "{dto.coupon.value.notnull}")
        @DecimalMin(value = "0.0", message = "{dto.coupon.value.min}")
        BigDecimal value,
        @FutureOrPresent(message = "{dto.coupon.valid.from.future}")
        @NotNull(message = "{dto.coupon.valid.from.notnull}")
        LocalDate validFrom,
        @Future(message = "{dto.coupon.valid.until.future}")
        @NotNull(message = "{dto.coupon.valid.until.notnull}")
        LocalDate validUntil,
        @Min(value = 1, message = "{dto.coupon.max.uses.min}")
        Integer maxUses,
        List<UUID> productIds,
        List<UUID> categoryIds
        ){
}
