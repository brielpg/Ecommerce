package br.com.api.ecommerce.models;

import br.com.api.ecommerce.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;
    private BigDecimal subTotal;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    private LocalDate timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDate.now();
    }

    public void updateSubTotal() {
        if (this.product != null && this.quantity != null) {
            this.subTotal = this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    public void incrementQuantity(Integer quantity) {
        this.quantity += quantity;
        updateSubTotal();
    }

    public void decrementQuantity(Integer quantity) {
        if (quantity > this.quantity) {
            throw new BadRequestException("exception.cart.quantity.not.available");
        }
        this.quantity -= quantity;
        updateSubTotal();
    }
}
