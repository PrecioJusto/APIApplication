package app.preciojusto.application.services;


import app.preciojusto.application.dto.ShoppingCartRequestDTO;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.ResourceAlreadyExistsException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserService userService;

    @Override
    public Optional<ShoppingCart> findByShopid(Long shopid) {
        return this.shoppingCartRepository.findById(shopid);
    }

    @Override
    public List<ShoppingCart> findShoppingCartsByUser(Long userid) {
        return this.shoppingCartRepository.findShoppingCartsByUser_Userid(userid);
    }

    @Override
    public ShoppingCart saveShoppingCart(ShoppingCartRequestDTO request) throws ResourceNotFoundException {
        final ShoppingCart shoppingCart;
        if (request.getShopid() != null) {
            shoppingCart = this.shoppingCartRepository.findById(request.getShopid())
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));
        } else {
            shoppingCart = new ShoppingCart();
        }

        shoppingCart.setShopname(request.getShopname());

        if (request.getShopid() == null) shoppingCart.setShopcreationdate(LocalDateTime.now());
        shoppingCart.setShoplastupdated(LocalDateTime.now());


        if (request.getUserid() != null) {
            shoppingCart.setUser(this.userService.findById(request.getUserid())
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR)));
        }
        try {
            return this.shoppingCartRepository.save(shoppingCart);
        } catch (final Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.SHOPPINGCART_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public Boolean delete(Long id) throws ResourceNotFoundException {
            this.shoppingCartRepository.delete(this.findByShopid(id)
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR)));
            return true;
    }
}
