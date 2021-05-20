package app.preciojusto.application.services;

import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.entities.User;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

public interface TokenService {
    String generateNewToken(User user);
    Map<String, Claim> getSubject(String token);
}
