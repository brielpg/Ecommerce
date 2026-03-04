package br.com.api.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<Product> products;
    private Boolean active;
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDateTime.now();
        this.active = true;
    }
}
