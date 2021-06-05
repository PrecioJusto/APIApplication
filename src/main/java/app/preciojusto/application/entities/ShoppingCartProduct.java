package app.preciojusto.application.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShoppingCartProduct {

    @EmbeddedId
    private ShoppingCartProductCK id;

    @ManyToOne
    @MapsId("prodid")
    @JoinColumn(name = "prodid")
    private Product product;

    @ManyToOne
    @MapsId("shopid")
    @JoinColumn(name = "shopid")
    private ShoppingCart shoppingCart;

    private Integer shprquantity;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ShoppingCartProduct that = (ShoppingCartProduct) o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.shoppingCart, that.shoppingCart) && Objects.equals(this.product, that.product) && Objects.equals(this.shprquantity, that.shprquantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.shoppingCart, this.product, this.shprquantity);
    }
}
