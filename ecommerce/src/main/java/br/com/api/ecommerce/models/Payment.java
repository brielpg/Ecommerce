package br.com.api.ecommerce.models;

import br.com.api.ecommerce.models.enums.PaymentMethods;
import br.com.api.ecommerce.models.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    @JsonIgnore
    private Order order;
    private BigDecimal totalPrice;
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDateTime.now();
    }
}
