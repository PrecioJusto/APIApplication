package app.preciojusto.application.dto;

import app.preciojusto.application.entities.User;
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
    private User user;
}
