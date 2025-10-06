package br.com.api.ecommerce.models.dtos.Coupon;

import br.com.api.ecommerce.models.enums.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CouponDtoUpdate(
        @NotNull(message = "{dto.coupon.code.notblank}")
        UUID id,
        String code,
        String description,
        DiscountType discountType,
        @DecimalMin(value = "0.0", message = "{dto.coupon.value.min}")
        BigDecimal value,
        @FutureOrPresent(message = "{dto.coupon.valid.from.future}")
        LocalDate validFrom,
        @Future(message = "{dto.coupon.valid.until.future}")
        LocalDate validUntil,
        @Min(value = 1, message = "{dto.coupon.max.uses.min}")
        Integer maxUses,
        List<UUID> productIds,
        List<UUID> categoryIds
){
}
