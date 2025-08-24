package br.com.api.ecommerce.models;

import br.com.api.ecommerce.models.enums.PaymentMethods;
import br.com.api.ecommerce.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_payments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private BigDecimal totalPrice;
    private LocalDate timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDate.now();
    }
}
