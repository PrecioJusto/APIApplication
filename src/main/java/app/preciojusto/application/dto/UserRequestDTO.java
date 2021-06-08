package app.preciojusto.application.dto;

import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.entities.enums.UserGender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    private Long userid;

    private String username;

    private String usersurname;

    private UserGender usergender;

    private String olduserpass;

    private String userpass;

    private String repeatuserpass;

    private String useremail;

    private String userphonenumber;

    private String userImage;

    private Boolean usernative;

}
