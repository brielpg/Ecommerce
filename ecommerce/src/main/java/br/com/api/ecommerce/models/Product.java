package br.com.api.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_products")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Product extends BaseEntity{

    private String description;
    private BigDecimal price;
    private Integer stock;
    private Double rating = 0.0;
    private Integer purchaseCount = 0;
    private Boolean active = true;

    @Column(unique = true)
    private String name;

    @Lob
    private byte[] image;

    @ManyToMany
    @JoinTable(name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}
