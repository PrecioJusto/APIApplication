package app.preciojusto.application.repositories;

import app.preciojusto.application.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
