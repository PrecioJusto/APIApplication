package app.preciojusto.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDTO {
    private Long prodid;
    private String prodname;
    private LocalDateTime prodcreatedtime;
    private Long prodviews;
    private BrandDTO brand;
    private CategoryDTO category;
}
