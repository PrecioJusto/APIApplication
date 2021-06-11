package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.ProductRequestDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.entities.Product;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.BadRequestException;
import app.preciojusto.application.exceptions.UnauthorizedException;
import app.preciojusto.application.services.ProductService;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/api/favourite")
    public List<ProductResponseDTO> getFavouriteProducts(@RequestAttribute Map<String, Claim> userToken) throws Exception {
        Long userid = userToken.get("userid").asLong();
        if (userid == null) throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);
        return this.productService.getFavouriteProducts(userid);
    }

    @PostMapping("/api/favourite")
    public Product postAddFavouriteProduct(@RequestAttribute Map<String, Claim> userToken, @RequestBody ProductRequestDTO request) {
        if (request.getProdid() == null || request.getUseridfavourite() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        if (!request.getUseridfavourite().equals(userToken.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        return this.productService.addAsFavourite(request);
    }

    @DeleteMapping("/api/favourite/{userid}/{prodid}")
    public Boolean deleteFavouriteProduct(@PathVariable Long userid, @PathVariable Long prodid, @RequestAttribute Map<String, Claim> userToken) {
        if (userid == null || prodid == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        if (!userid.equals(userToken.get("userid").asLong())) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);
        return this.productService.deleteFavourite(userid, prodid);
    }
}
