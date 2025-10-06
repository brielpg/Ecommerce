package br.com.api.ecommerce.models;

import br.com.api.ecommerce.models.enums.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String code;
    private String description;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private BigDecimal value;
    private LocalDate validFrom;
    private LocalDate validUntil;
    private Integer maxUses;
    private Integer usedCount = 0;
    @ManyToMany
    @JoinTable(name = "tb_coupon_products",
            joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonIgnore
    private List<Product> products;
    @ManyToMany
    @JoinTable(name = "tb_coupon_categories",
            joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnore
    private List<Category> categories;

    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return (validFrom == null || !today.isBefore(validFrom)) &&
                (validUntil == null || !today.isAfter(validUntil)) &&
                (maxUses == null || usedCount < maxUses);
    }
}
