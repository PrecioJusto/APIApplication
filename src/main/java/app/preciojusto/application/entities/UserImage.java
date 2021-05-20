package app.preciojusto.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usimid;

    @Column(unique = true, nullable = false)
    private String usimname;

    @Lob
    @Column(nullable = false)
    private byte [] usimimage;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userid")
    @JsonIgnore
    private User user;
}
