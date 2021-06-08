package app.preciojusto.application.dto.mappers;

import app.preciojusto.application.dto.BrandDTO;
import app.preciojusto.application.dto.CategoryDTO;
import app.preciojusto.application.dto.ProductResponseDTO;
import com.google.gson.Gson;
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
        productResponseDTO.setCategory(mapperCategoryDTO(map.get("category").toString()));
        productResponseDTO.setBrand(mapperBrandDTO(map.get("brand").toString()));
        return productResponseDTO;
    }

    public CategoryDTO mapperCategoryDTO(String category) {
        Map<String, Object> categoryMap = new Gson().fromJson(category, HashMap.class);
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCateid((long) Double.parseDouble(categoryMap.get("cateid").toString()));
        categoryDTO.setCatename(categoryMap.get("catename").toString());
        return categoryDTO;
    }

    public BrandDTO mapperBrandDTO(String brand) {
        Long branid = getBranidFromString(brand);
        String branname = getBrannameFromString(brand);

        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBranid(branid);
        brandDTO.setBranname(branname);
        return brandDTO;
    }

    private Long getBranidFromString(String brand) {
        return (long) Double.parseDouble(brand.split("=")[1].split(",")[0]);
    }

    private String getBrannameFromString(String brand) {
        StringBuilder sb = new StringBuilder(brand);
        String brandParsed = sb.deleteCharAt(brand.length() - 1).toString();
        return brandParsed.split("=")[2];
    }
}
