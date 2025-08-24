package br.com.api.ecommerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String name;
    private String description;
    @ManyToMany(mappedBy = "categories")
    private List<Product> products;
    private Boolean active;
    private LocalDate timestamp;

    @PrePersist
    public void prePersist(){
        this.timestamp = LocalDate.now();
        this.active = true;
    }
}
