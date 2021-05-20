package app.preciojusto.application.repositories;

import app.preciojusto.application.entities.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUsimname(String name);
    Boolean existsByUsimname(String name);

}
