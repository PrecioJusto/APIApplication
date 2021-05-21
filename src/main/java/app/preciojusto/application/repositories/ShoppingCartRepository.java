package app.preciojusto.application.repositories;

import app.preciojusto.application.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findShoppingCartsByUser_Userid(Long userid);
}
