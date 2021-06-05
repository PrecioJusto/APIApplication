package app.preciojusto.application.repositories;

import app.preciojusto.application.entities.ShoppingCartProduct;
import app.preciojusto.application.entities.ShoppingCartProductCK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, ShoppingCartProductCK> {
    List<ShoppingCartProduct> findAllByShoppingCart_Shopid(Long shopid);
}
