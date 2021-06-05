package app.preciojusto.application.services;

import app.preciojusto.application.dto.ProductRequestDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {


    Product addAsFavourite(ProductRequestDTO request);
    Boolean deleteFavourite(Long userid, Long prodid);

    List<ProductResponseDTO> getFavouriteProducts(Long userid) throws Exception;
    //Connection
    List<ProductResponseDTO> findAllProductsByIds(List<Long> ids) throws Exception;
    Optional<ProductResponseDTO> findByIdInAPIProducts(Long id);

    Product save(Product product);

    Optional<Product> findById(Long prodid);
}
