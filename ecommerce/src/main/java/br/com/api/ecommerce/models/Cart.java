package br.com.api.ecommerce.models;

import br.com.api.ecommerce.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_carts")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Cart extends BaseEntity{

    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    public void addItem(Item newItem) {
        this.items.stream()
                .filter(item -> item.getProduct().getId().equals(newItem.getProduct().getId()))
                .findFirst()
                .ifPresentOrElse(
                        itemExistente -> itemExistente.incrementQuantity(newItem.getQuantity()),
                        () -> {
                            newItem.setCart(this);
                            this.items.add(newItem);
                            newItem.updateSubTotal();
                        }
                );
        recalculateTotal();
    }

    public void removeItem(UUID productId, Integer quantity) {
        Item item = this.items.stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("exception.cart.product.not.in"));

        if (quantity >= item.getQuantity()) {
            this.items.remove(item);
        } else {
            item.decrementQuantity(quantity);
        }
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalPrice = this.items.stream()
                .map(Item::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
