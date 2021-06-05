package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.ShoppingCartProductRequestDTO;
import app.preciojusto.application.dto.ShoppingCartRequestDTO;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.entities.ShoppingCartProduct;
import app.preciojusto.application.entities.ShoppingCartProductCK;
import app.preciojusto.application.entities.User;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.BadRequestException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.exceptions.UnauthorizedException;
import app.preciojusto.application.services.ShoppingCartProductService;
import app.preciojusto.application.services.ShoppingCartService;
import com.auth0.jwt.interfaces.Claim;
import com.google.gson.annotations.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ShoppingCartProductController {

    @Autowired
    private ShoppingCartProductService shoppingCartProductService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/shoppingcartproduct")
    public ShoppingCartProduct postAddShoppingCartProduct(@RequestBody ShoppingCartProductRequestDTO request, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        checkBadRequest(request);

        ShoppingCart shoppingCart = this.shoppingCartService.findByShopid(request.getShopid())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));

        if (!shoppingCart.getUser().getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        return this.shoppingCartProductService.add(request);
    }

    @PutMapping("/shoppingcartproduct")
    public ShoppingCartProduct putUpdateShoppingCartProduct(@RequestBody ShoppingCartProductRequestDTO request, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        checkBadRequest(request);

        ShoppingCart shoppingCart = this.shoppingCartService.findByShopid(request.getShopid())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));

        if (!shoppingCart.getUser().getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        return this.shoppingCartProductService.update(request);
    }

    @DeleteMapping("/shoppingcartproduct/{productId}/{shoppingCartId}")
    public Boolean deleteShoppingCartProduct(@PathVariable Long productId, @PathVariable Long shoppingCartId, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        ShoppingCart shoppingCart = this.shoppingCartService.findByShopid(shoppingCartId)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));

        if (!shoppingCart.getUser().getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return this.shoppingCartProductService.delete(productId, shoppingCartId);
    }


    private void checkBadRequest(ShoppingCartProductRequestDTO request) {
        if (request.getProdid() == null || request.getShopid() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
    }
}
