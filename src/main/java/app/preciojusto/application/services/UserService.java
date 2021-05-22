package app.preciojusto.application.services;

import app.preciojusto.application.dto.LoginRequestDTO;
import app.preciojusto.application.dto.LoginResponseDTO;
import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.entities.User;
import app.preciojusto.application.exceptions.ResourceNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long userid);
    LoginResponseDTO loginUser(LoginRequestDTO user) throws Exception;
    Optional<User> findUserByEmail(String email);
    User createUser(UserRequestDTO user) throws InvalidKeySpecException, NoSuchAlgorithmException;
    User updateUser(UserRequestDTO user) throws ResourceNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException;
    User disableUser(UserRequestDTO user) throws ResourceNotFoundException;
    void checkRegister(UserRequestDTO request) throws ResourceNotFoundException;
    void checkUpdateProfile(UserRequestDTO request);
}
