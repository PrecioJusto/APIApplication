package app.preciojusto.application.dto;

import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.entities.enums.UserGender;

public class UserResponseDTO {
    private Long userid;
    private String username;
    private UserGender usergender;
    private String useremail;
    private String userphonenumber;
    private String userImage;
}
