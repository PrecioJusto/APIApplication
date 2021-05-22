package app.preciojusto.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartRequestDTO {

    private Long shopid;
    private String shopname;
    private Long userid;
}
