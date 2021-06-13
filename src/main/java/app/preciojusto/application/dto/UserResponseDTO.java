package app.preciojusto.application.dto;

import app.preciojusto.application.entities.Product;
import app.preciojusto.application.entities.ShoppingCart;
import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.entities.enums.UserGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {

    private Long userid;
    private String username;
    private String usersurname;
    private UserGender usergender;
    private String useremail;
    private String userphonenumber;
    private UserImage userImage;
    private Boolean usernative;
}
