package app.preciojusto.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private Long prodid;

    @ManyToMany
    @JoinTable(
            name = "userproduct",
            joinColumns = @JoinColumn(name = "prodid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    private Set<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    Set<ShoppingCartProduct> shoppingCartProducts;
}
