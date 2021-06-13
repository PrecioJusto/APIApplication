package app.preciojusto.application.dto;

import app.preciojusto.application.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartResponseDTO {
    private Long shopid;
    private String shopname;
    private LocalDateTime shopcreationdate;
    private List<ProductResponseDTO> products;

    @JsonIgnore
    private User user;
}
