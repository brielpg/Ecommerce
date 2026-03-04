package br.com.api.ecommerce.models;

import br.com.api.ecommerce.models.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_users")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class User extends BaseEntity implements UserDetails {

    private String name;
    private String phone;
    private LocalDate birthDate;
    private Boolean active = true;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    @ManyToMany
    @JoinTable(name = "tb_user_favorites",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonIgnore
    private List<Product> favorites;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles role = UserRoles.CUSTOMER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = this.role == UserRoles.CUSTOMER ? "ROLE_USER" : "ROLE_" + this.role.name();
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
