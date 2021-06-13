package app.preciojusto.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SupermarketProductDTO {
    private Integer suprprice;
    private String suprimg;
    private String supename;

    public SupermarketProductDTO(Integer suprprice, String suprimg, String supename) {
        this.suprprice = suprprice;
        this.suprimg = suprimg;
        this.supename = supename;
    }
}
