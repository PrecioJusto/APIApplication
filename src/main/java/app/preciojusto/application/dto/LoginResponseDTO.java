package app.preciojusto.application.dto;

import app.preciojusto.application.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private User user;
}
