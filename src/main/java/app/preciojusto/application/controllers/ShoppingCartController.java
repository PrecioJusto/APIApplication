package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.ShoppingCartRequestDTO;
import app.preciojusto.application.dto.ShoppingCartResponseDTO;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.BadRequestException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.exceptions.UnauthorizedException;
import app.preciojusto.application.services.ShoppingCartService;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/api/shoppingcarts")
    public List<ShoppingCart> getShoppingCartsByUser(@RequestAttribute Map<String, Claim> userToken) {

        //if (!id.equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return this.shoppingCartService.findShoppingCartsByUser(userToken.get("userid").asLong());
    }

    @GetMapping("/api/shoppingcart/{id}")
    public ShoppingCartResponseDTO getShoppingCart(@PathVariable Long id, @RequestAttribute Map<String, Claim> userToken) throws Exception {
        ShoppingCartResponseDTO shoppingCart = this.shoppingCartService.findByShopidDTO(id);
        if (!shoppingCart.getUser().getUserid().equals(userToken.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return shoppingCart;
    }

    @PostMapping("/api/shoppingcart")
    public ShoppingCart postAddShoppingCart(@RequestBody ShoppingCartRequestDTO request, @RequestAttribute Map<String, Claim> userToken) throws ResourceNotFoundException {

        if (!request.getUserid().equals(userToken.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        if (request.getShopid() != null || request.getShopname() == null || request.getUserid() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
        return this.shoppingCartService.saveShoppingCart(request);
    }

    @PutMapping("/api/shoppingcart")
    public ShoppingCart putUpdateShoppingCart(@RequestBody ShoppingCartRequestDTO request, @RequestAttribute Map<String, Claim> userToken) throws ResourceNotFoundException {

        if (!request.getUserid().equals(userToken.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);


        if (request.getShopid() == null || request.getShopname() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
        return this.shoppingCartService.saveShoppingCart(request);
    }

    @DeleteMapping("/api/shoppingcart/{id}")
    public Boolean deleteShoppingCart(@PathVariable Long id, @RequestAttribute Map<String, Claim> userToken) throws ResourceNotFoundException {
        return this.shoppingCartService.delete(id, userToken.get("userid").asLong());
    }
}
