package app.preciojusto.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopid;


    @Column(nullable = false)
    private String shopname;

    @Column(nullable = false)
    private LocalDateTime shopcreationdate;

    @Column(nullable = false)
    private LocalDateTime shoplastupdated;

    @ManyToOne
    @JoinColumn(name="userid", nullable=false)
    @JsonIgnore
    private User user;

}
