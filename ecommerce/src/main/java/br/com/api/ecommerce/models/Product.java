package br.com.api.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Double rating;
    private Integer purchaseCount;

    @Lob
    private byte[] image;

    @ManyToMany
    @JoinTable(name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    private Boolean active;
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDateTime.now();
        this.purchaseCount = 0;
        this.active = true;
        this.rating = 0.0;
    }
}
