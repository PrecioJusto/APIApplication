package app.preciojusto.application.services;

import app.preciojusto.application.dto.ShoppingCartRequestDTO;
import app.preciojusto.application.dto.ShoppingCartResponseDTO;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

    ShoppingCartResponseDTO findByShopidDTO(Long shopid) throws Exception;
    Optional<ShoppingCart> findByShopid(Long shopid);

    List<ShoppingCart> findShoppingCartsByUser(Long userid);
    ShoppingCart saveShoppingCart(ShoppingCartRequestDTO shoppingCartRequestDTO) throws ResourceNotFoundException;

    Boolean delete(Long shopid, Long userid) throws ResourceNotFoundException;
}
