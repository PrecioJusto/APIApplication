package app.preciojusto.application.services;

import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${token.secret}")
    String tokenSecret;

    @Value("${token.expiration.time}")
    Long tokenExpirationTime;


    @Override
    public String generateNewToken(User user) {
        return JWT.create()
                .withClaim("userid", user.getUserid())
                .withClaim("useremail", user.getUseremail())
                .withClaim("username", user.getUsername())
                .withClaim("iat", System.currentTimeMillis() / 1000)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .sign(Algorithm.HMAC256(tokenSecret.getBytes()));
    }



    @Override
    public Map<String, Claim> getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(tokenSecret.getBytes()))
                .build()
                .verify(token)
                .getClaims();
    }
}
