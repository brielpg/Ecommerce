package br.com.api.ecommerce.models;

import br.com.api.ecommerce.models.enums.PaymentMethods;
import br.com.api.ecommerce.models.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_payments")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Payment extends BaseEntity{

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
