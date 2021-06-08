package app.preciojusto.application.services;


import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.dto.ShoppingCartRequestDTO;
import app.preciojusto.application.dto.ShoppingCartResponseDTO;
import app.preciojusto.application.entities.Product;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.entities.ShoppingCartProduct;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.ResourceAlreadyExistsException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.exceptions.UnauthorizedException;
import app.preciojusto.application.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartProductService shoppingCartProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Override
    public ShoppingCartResponseDTO findByShopidDTO(Long shopid) throws Exception {

        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(shopid)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));

        List<ShoppingCartProduct> shoppingCartProducts = this.shoppingCartProductService.findShoppingCartByShopid(shopid);
        List<Long> shoppingCartProductIds = new ArrayList<>();
        for (ShoppingCartProduct scp: shoppingCartProducts) {
            shoppingCartProductIds.add(scp.getProduct().getProdid());
        }

        List<ProductResponseDTO> productsFromShoppingCart = this.productService.findAllProductsByIds(shoppingCartProductIds);

        ShoppingCartResponseDTO shoppingCartResponseDTO = new ShoppingCartResponseDTO();
        shoppingCartResponseDTO.setShopid(shoppingCart.getShopid());
        shoppingCartResponseDTO.setShopname(shoppingCart.getShopname());
        shoppingCartResponseDTO.setProducts(productsFromShoppingCart);
        shoppingCartResponseDTO.setUser(shoppingCart.getUser());
        return shoppingCartResponseDTO;
    }

    @Override
    public Optional<ShoppingCart> findByShopid(Long shopid) {
        return this.shoppingCartRepository.findById(shopid);
    }

    @Override
    public List<ShoppingCart> findShoppingCartsByUser(Long userid) {
        return this.shoppingCartRepository.findShoppingCartsByUser_Userid(userid);
    }

    @Transactional
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

    @Transactional
    @Override
    public Boolean delete(Long shopid, Long userid) throws ResourceNotFoundException {


        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(shopid)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR));

        if (!shoppingCart.getUser().getUserid().equals(userid)) throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

            this.shoppingCartRepository.delete(this.findByShopid(shopid)
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCART_NOT_FOUND_ERROR)));
            return true;
    }
}
