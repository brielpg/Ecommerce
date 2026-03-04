package br.com.api.ecommerce.models;

import br.com.api.ecommerce.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_items")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Item extends BaseEntity{

    private Integer quantity;
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

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
