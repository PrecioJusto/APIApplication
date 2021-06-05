package app.preciojusto.application.services;

import app.preciojusto.application.dto.ProductResponseDTO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public interface ConnectionService {


    List<ProductResponseDTO> getProducts(List<Long> ids) throws Exception;
}
