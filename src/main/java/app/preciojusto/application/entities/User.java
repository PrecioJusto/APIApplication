package app.preciojusto.application.entities;

import app.preciojusto.application.entities.enums.UserGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String usersurname;

    @Enumerated(EnumType.STRING)
    @Column(name = "usergender")
    private UserGender usergender;

    @JsonIgnore
    private String userpass;

    @Column(nullable = false)
    private boolean useractive;

    @Column(unique = true, nullable = false)
    private String useremail;

    private String userphonenumber;

    @Column(nullable = false)
    private Boolean usernative;

    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserImage userImage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ShoppingCart> shoppingCarts;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;
}
