package app.preciojusto.application.services;

import app.preciojusto.application.entities.User;
import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.exceptions.ApplicationExceptionCode;
import app.preciojusto.application.exceptions.ResourceAlreadyExistsException;
import app.preciojusto.application.exceptions.ResourceNotFoundException;
import app.preciojusto.application.repositories.UserImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class UserImageServiceImpl implements UserImageService{
    @Autowired
    UserImageRepository userImageRepository;

    @Autowired
    private UserService userService;

    @Override
    public byte[] findDataImageByName(String name) throws ResourceNotFoundException {
        UserImage userImage = this.userImageRepository.findByUsimname(name)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.IMAGE_NOT_FOUND_ERROR));
        return userImage.getUsimimage();
    }

    @Override
    public Boolean existsByUsimname(String name) {
        return userImageRepository.existsByUsimname(name);
    }

    @Override
    public Optional<UserImage> findByName(String name) {
        return userImageRepository.findByUsimname(name);
    }

    @Override
    public Optional<UserImage> findById(Long userImageId) {
        return this.userImageRepository.findById(userImageId);
    }

    @Override
    public Boolean delete(Long userImageId) {
        try {
            this.userImageRepository.delete(this.findById(userImageId)
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.IMAGE_NOT_FOUND_ERROR)));
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    @Override
    public UserImage save(UserImage request) throws ResourceNotFoundException {
        UserImage userImage;
        if (request.getUsimid() != null) userImage = this.userImageRepository.findById(request.getUsimid())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.IMAGE_NOT_FOUND_ERROR));
        else
            userImage = new UserImage();
        userImage.setUsimimage(request.getUsimimage());
        userImage.setUsimname(request.getUsimname());

        if (request.getUser() != null)
            userImage.setUser(this.userService.findById(request.getUser().getUserid())
                    .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR)));
        try {
            return this.userImageRepository.save(userImage);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.IMAGE_ALREADY_EXISTS_ERROR);
        }
    }
}
