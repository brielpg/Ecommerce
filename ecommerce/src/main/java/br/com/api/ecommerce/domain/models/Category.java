package br.com.api.ecommerce.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_categories")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Category extends BaseEntity{

    private String description;
    private Boolean active = true;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private Set<Product> products = new HashSet<>();
}
