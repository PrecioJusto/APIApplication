package app.preciojusto.application.services;

import app.preciojusto.application.dto.ShoppingCartProductRequestDTO;
import app.preciojusto.application.entities.Product;
import app.preciojusto.application.entities.ShoppingCartProduct;
import app.preciojusto.application.entities.ShoppingCartProductCK;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.ResourceAlreadyExistsException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.repositories.ShoppingCartProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartProductServiceImpl implements ShoppingCartProductService{

    @Autowired
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Override
    public Optional<ShoppingCartProduct> findById(ShoppingCartProductCK id) {
        return this.shoppingCartProductRepository.findById(id);
    }

    @Override
    public List<ShoppingCartProduct> findShoppingCartByShopid(Long shopid) {
        return this.shoppingCartProductRepository.findAllByShoppingCart_Shopid(shopid);
    }

    @Override
    public ShoppingCartProduct add(ShoppingCartProductRequestDTO request) {
        if (this.findById(new ShoppingCartProductCK(request.getProdid(), request.getShopid())).isPresent())
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_ALREADY_EXISTS_ERROR);
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();

        shoppingCartProduct.setId(new ShoppingCartProductCK(request.getProdid(), request.getShopid()));
        shoppingCartProduct.setShprquantity(request.getShprquantity());



        this.productService.findById(request.getProdid()).ifPresentOrElse(
                shoppingCartProduct::setProduct,
                () -> {
                    Product product = new Product();
                    product.setProdid(request.getProdid());
                    shoppingCartProduct.setProduct(this.productService.save(product));
                });


        this.shoppingCartService.findByShopid(request.getShopid()).ifPresentOrElse(
                shoppingCartProduct::setShoppingCart,
                () -> {
                    throw new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_NOT_FOUND_ERROR);
                });
        try {
            return this.shoppingCartProductRepository.save(shoppingCartProduct);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public ShoppingCartProduct update(ShoppingCartProductRequestDTO request) throws ResourceNotFoundException {
        ShoppingCartProduct shoppingCartProduct = this.findById(new ShoppingCartProductCK(request.getProdid(), request.getShopid()))
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_NOT_FOUND_ERROR));

        shoppingCartProduct.setShprquantity(request.getShprquantity());
        try {
            return this.shoppingCartProductRepository.save(shoppingCartProduct);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public Boolean delete(Long productId, Long shoppingCartId) {
        try {
            this.shoppingCartProductRepository.delete(this.findById(new ShoppingCartProductCK(productId, shoppingCartId))
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.SHOPPINGCARTPRODUCT_NOT_FOUND_ERROR)));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
