package app.preciojusto.application.dto.mappers;

import app.preciojusto.application.dto.BrandDTO;
import app.preciojusto.application.dto.CategoryDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import app.preciojusto.application.dto.SupermarketProductDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductResponseMapper {

    public List<ProductResponseDTO> mapperList(List<Map<String, Object>> maps) {
        List<ProductResponseDTO> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            if (map == null) result.add(null);
            else result.add(this.mapper(map));
        }
        return result;
    }

    public ProductResponseDTO mapper(Map<String, Object> map) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setProdid((long) Double.parseDouble(map.get("prodid").toString()));
        productResponseDTO.setProdname(map.get("prodname").toString());
        productResponseDTO.setProdviews((long) Double.parseDouble(map.get("prodviews").toString()));

        productResponseDTO.setCategory(new Gson().fromJson(new Gson().toJson(map.get("category")), CategoryDTO.class));
        productResponseDTO.setBrand(new Gson().fromJson(new Gson().toJson(map.get("brand")), BrandDTO.class));
        productResponseDTO.setSupermarketProducts(mapperSupermarketProducts(new Gson().fromJson(new Gson().toJson(map.get("supermarketProducts")), ArrayList.class)));
        return productResponseDTO;
    }
    public List<SupermarketProductDTO> mapperSupermarketProducts(List<Map<String, Object>> supermarketProductsMap) {
        List<SupermarketProductDTO> supermarketProductDTOList = new ArrayList<>();

        supermarketProductsMap.forEach(sp -> {
            String supermarketName = getSupernameFromString(sp.get("supeid").toString());
            String image = null;
            if (sp.get("suprimg") != null) image = sp.get("suprimg").toString();
            supermarketProductDTOList.add(new SupermarketProductDTO((int) Double.parseDouble(sp.get("suprprice").toString()), image, supermarketName));
        });
        return supermarketProductDTOList;
    }

    private String getSupernameFromString(String supeid) {
        StringBuilder sb = new StringBuilder(supeid);
        String brandParsed = sb.deleteCharAt(supeid.length() - 1).toString();
        return brandParsed.split("=")[2];
    }
}
