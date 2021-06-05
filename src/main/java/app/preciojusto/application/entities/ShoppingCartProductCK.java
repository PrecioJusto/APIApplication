package app.preciojusto.application.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ShoppingCartProductCK implements Serializable {
    @Column(name = "prodid")
    private Long prodid;

    @Column(name = "shopid")
    private Long shopid;

    public ShoppingCartProductCK(Long prodid, Long shopid) {
        this.prodid = prodid;
        this.shopid = shopid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ShoppingCartProductCK that = (ShoppingCartProductCK) o;
        return Objects.equals(this.shopid, that.shopid) && Objects.equals(this.prodid, that.prodid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shopid, this.prodid);
    }
}
