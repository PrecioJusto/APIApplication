package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.ShoppingCartRequestDTO;
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

    @GetMapping("/shoppingcart/{id}/user")
    public List<ShoppingCart> getShoppingCartsByUser(@PathVariable Long id, @RequestAttribute Map<String, Claim> user) {

        if (!id.equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return this.shoppingCartService.findShoppingCartsByUser(id);
    }

    @GetMapping("/shoppingcart/{id}/get")
    public ShoppingCart getShoppingCart(@PathVariable Long id, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        ShoppingCart shoppingCart = this.shoppingCartService.findByShopid(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));

        if (!shoppingCart.getUser().getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return shoppingCart;
    }

    @PostMapping("/shoppingcart/add")
    public ShoppingCart postAddShoppingCart(@RequestBody ShoppingCartRequestDTO request, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {

        if (!request.getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        if (request.getShopid() != null || request.getShopname() == null || request.getUserid() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
        return this.shoppingCartService.saveShoppingCart(request);
    }

    @PutMapping("/shoppingcart/update")
    public ShoppingCart putUpdateShoppingCart(@RequestBody ShoppingCartRequestDTO request, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {

        if (!request.getUserid().equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);


        if (request.getShopid() == null || request.getShopname() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
        return this.shoppingCartService.saveShoppingCart(request);
    }

    @DeleteMapping("/shoppingcart/{id}/delete")
    public Boolean deleteShoppingCart(@PathVariable Long id, @RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        if (!id.equals(user.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return this.shoppingCartService.delete(id);
    }
}
