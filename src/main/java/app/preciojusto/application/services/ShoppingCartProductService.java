package app.preciojusto.application.services;


import app.preciojusto.application.dto.ShoppingCartProductRequestDTO;
import app.preciojusto.application.entities.ShoppingCartProduct;
import app.preciojusto.application.entities.ShoppingCartProductCK;
import app.preciojusto.application.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartProductService {
    Optional<ShoppingCartProduct> findById(ShoppingCartProductCK id);

    List<ShoppingCartProduct> findShoppingCartByShopid(Long shopid);

    ShoppingCartProduct add(ShoppingCartProductRequestDTO request);

    ShoppingCartProduct update(ShoppingCartProductRequestDTO request) throws ResourceNotFoundException;

    Boolean delete (Long productId, Long shoppingCartId);
}
