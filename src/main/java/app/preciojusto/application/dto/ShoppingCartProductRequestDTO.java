package app.preciojusto.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartProductRequestDTO {

    private Long prodid;

    private Long shopid;

    private Integer shprquantity;

}
