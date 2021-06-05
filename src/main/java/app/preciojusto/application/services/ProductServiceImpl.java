package app.preciojusto.application.services;

import app.preciojusto.application.dto.ProductRequestDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.entities.*;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.ResourceAlreadyExistsException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @Override
    public Product addAsFavourite(ProductRequestDTO request) {
        Product product;
        Optional<Product> op = this.productRepository.findById(request.getProdid());
        if(op.isPresent()) {
            product = op.get();
        } else {
            Product product1 = new Product();
            product1.setProdid(request.getProdid());
            product = this.productRepository.save(product1);
            product.setUsers(new HashSet<>());
        }

        if (request.getUseridfavourite() != null) {
            product.getUsers().add(this.userService.findById(request.getUseridfavourite())
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR)));
        }

        try {
            return this.productRepository.save(product);
        } catch (final Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.PRODUCT_ALREADY_EXISTS_ERROR);
        }

    }

    @Override
    public Boolean deleteFavourite(Long userid, Long prodid) {
        Product product = this.productRepository.findById(prodid)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.PRODUCT_NOT_FOUND_ERROR));
        User user = this.userService.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));


        if(product.getUsers().contains(user)) {
            //product.getUsers().remove(user);
            user.getProducts().remove(product);
            return true;
        }

        return false;
    }

    @Override
    public List<ProductResponseDTO> getFavouriteProducts(Long userid) throws Exception {
        User user = this.userService.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));

        List<Long> shoppingCartProductIds = new ArrayList<>();
        for (Product product: user.getProducts()) {
            shoppingCartProductIds.add(product.getProdid());
        }

        return this.findAllProductsByIds(shoppingCartProductIds);
    }

    @Override
    public List<ProductResponseDTO> findAllProductsByIds(List<Long> ids) throws Exception {
            return this.connectionService.getProducts(ids);
    }

    @Override
    public Optional<ProductResponseDTO> findByIdInAPIProducts(Long id) {
        return null;
    }

    @Override
    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long prodid) {
        return this.productRepository.findById(prodid);
    }

}
