package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.LoginRequestDTO;
import app.preciojusto.application.dto.LoginResponseDTO;
import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.entities.User;
import app.preciojusto.application.exceptions.*;
import app.preciojusto.application.services.TokenService;
import app.preciojusto.application.services.UserService;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;


    @GetMapping("/getprofile")
    public User getProfile(@RequestAttribute Map<String, Claim> user) throws ResourceNotFoundException {
        String email = user.get("useremail").asString();
        return this.userService.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));
    }

    @PostMapping("/register")
    public User postAddUser(@RequestBody UserRequestDTO request) throws InvalidKeySpecException, NoSuchAlgorithmException, ResourceNotFoundException {
        if (request.getUsername() == null || request.getUsergender() == null || request.getUseremail() == null ||
                request.getUserpass() == null || request.getUserid() != null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        //Validate data before create the user
        this.userService.checkRegister(request);
        return this.userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO userToLogin) throws Exception {

        if (userToLogin.getUseremail() == null || userToLogin.getUserpass() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        return this.userService.loginUser(userToLogin);
    }

    @PutMapping("/editprofile")
    public User putUpdateUser(@RequestBody UserRequestDTO request, @RequestAttribute Map<String, Claim> user) throws InvalidKeySpecException, NoSuchAlgorithmException, ResourceNotFoundException {

        if (!user.get("userid").asLong().equals(request.getUserid()))
            throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        if (request.getUsername() == null || request.getUserid() == null ||
                request.getUsergender() == null || request.getUseremail() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        //Validate data before update the user
        this.userService.checkRegister(request);
        return this.userService.updateUser(request);
    }
}
