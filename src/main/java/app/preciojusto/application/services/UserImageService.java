package app.preciojusto.application.services;

import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserImageService {
    byte [] findDataImageByName(String name) throws ResourceNotFoundException;
    Boolean existsByUsimname(String name);
    Optional<UserImage> findByName(String name);
    Optional<UserImage> findById(Long userImageId);
    Boolean delete(Long userImageId);
    UserImage save(UserImage userImage) throws ResourceNotFoundException;
}
