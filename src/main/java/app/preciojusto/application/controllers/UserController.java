package app.preciojusto.application.controllers;

import app.preciojusto.application.dto.LoginRequestDTO;
import app.preciojusto.application.dto.LoginResponseDTO;
import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.dto.UserResponseDTO;
import app.preciojusto.application.dto.mappers.UserMapper;
import app.preciojusto.application.entities.User;
import app.preciojusto.application.exceptions.*;
import app.preciojusto.application.services.*;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private FacebookService facebookService;

    @GetMapping("/api/profile")
    public UserResponseDTO getProfile(@RequestAttribute Map<String, Claim> userToken) throws ResourceNotFoundException {
        String email = userToken.get("useremail").asString();
        User user = this.userService.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));
        return UserMapper.INSTANCE.userToUserResponseDto(user);
    }

    @PostMapping("/api/register")
    public User postAddUser(@RequestBody UserRequestDTO request) throws InvalidKeySpecException, NoSuchAlgorithmException, ResourceNotFoundException {
        if (request.getUsername() == null || request.getUseremail() == null ||
                request.getUserpass() == null || request.getUserid() != null ||
                request.getUsersurname() == null || request.getUserpassrepeat() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        //Validate data before create the user
        this.userService.checkRegister(request);
        request.setUsernative(true);
        return this.userService.createUser(request);
    }

    @PostMapping("/api/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO userToLogin) throws Exception {

        if (userToLogin.getUseremail() == null || userToLogin.getUserpass() == null)
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        return this.userService.loginUser(userToLogin);
    }

    @PutMapping("/api/profile")
    public LoginResponseDTO putUpdateUser(@RequestBody UserRequestDTO request, @RequestAttribute Map<String, Claim> userToken) throws InvalidKeySpecException, NoSuchAlgorithmException, ResourceNotFoundException {

        if (!userToken.get("userid").asLong().equals(request.getUserid()))
            throw new UnauthorizedException(ApplicationExceptionCode.UNAUTHORIZED_ERROR);

        User userRequested = this.userService.findById(userToken.get("userid").asLong())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));

        if (userRequested.getUsernative() && (request.getUsername() == null || request.getUserid() == null ||
                request.getUseremail() == null || request.getOlduserpass() == null ||
                request.getUsersurname() == null || request.getUsernative() != null))
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);


        if (!userRequested.getUsernative() && (request.getUsername() == null || request.getUsersurname() == null ||
                request.getUserid() == null || request.getUsernative() != null))
            throw new BadRequestException(ApplicationExceptionCode.BADREQUEST_ERROR);

        //Validate data before update the user
        User userToReturn = null;
        if (userRequested.getUsernative()) {
            this.userService.checkUpdateProfile(request, userToken.get("useremail").asString());
            userToReturn = this.userService.updateUser(request);
        }
        if (!userRequested.getUsernative()) {
            this.userService.checkUpdateProfileOAuth(request);
            userToReturn = this.userService.updateUserOAuth(request);
        }

        if (userToReturn != null) {
            String token = this.tokenService.generateNewToken(userToReturn);
            LoginResponseDTO result = new LoginResponseDTO();
            result.setUser(userToReturn);
            result.setToken(token);
            return result;
        }
        throw new RuntimeException();
    }

    //OAuth Services

    @GetMapping("/api/usergoogle/{access_token}")
    public LoginResponseDTO getUserFromAccessToken(@PathVariable String access_token) {
        try {
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            Map<String, String> userDetails = this.googleService.getUserDetails(access_token);
            String emailGoogleAccount = userDetails.get("email");
            String username = userDetails.get("given_name");
            String family_name = userDetails.get("family_name");

            if (emailGoogleAccount == null || username == null || family_name == null)
                throw new OAuthServiceException(ApplicationExceptionCode.GOOGLE_SERVICE_ERROR);
            Optional<User> userExists = this.userService.findUserByEmail(emailGoogleAccount);

            String token = "";
            User user;
            if (userExists.isPresent()) {
                token = this.tokenService.generateNewToken(userExists.get());
                user = userExists.get();
            } else {
                UserRequestDTO userRequestDTO = new UserRequestDTO();
                userRequestDTO.setUseremail(emailGoogleAccount);
                userRequestDTO.setUsername(username);
                userRequestDTO.setUsergender(null);
                userRequestDTO.setUserpass(null);
                userRequestDTO.setUsernative(false);
                userRequestDTO.setUsersurname(family_name);
                userRequestDTO.setUserImage(null);
                userRequestDTO.setUserphonenumber(null);
                userRequestDTO.setUserpassrepeat(null);
                user = this.userService.createUser(userRequestDTO);
                token = this.tokenService.generateNewToken(user);
            }
            loginResponseDTO.setUser(user);
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } catch (Exception e) {
            throw new OAuthServiceException(ApplicationExceptionCode.GOOGLE_SERVICE_ERROR);
        }
    }

    @GetMapping("/api/loginTwitter")
    public RedirectView loginTwitter() {
        Map<String, String> requestToken = this.twitterService.getRequestToken();
        URL url = this.twitterService.getUrlRedirectTwitter(requestToken.get("oauth_token"));
        return new RedirectView(url.toString());
    }

    @GetMapping("/api/auth/twitter/oauth2callback/")
    public LoginResponseDTO twitterAuthCallback(@RequestParam String oauth_token, @RequestParam String oauth_verifier) {

        try {
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            Map<String, String> accessTokenUser = twitterService.getAccessToken(oauth_token, oauth_verifier);
            Map<String, Object> verify_credentials = twitterService.getAccountDetails(accessTokenUser.get("oauth_token"), accessTokenUser.get("oauth_token_secret"));
            String emailTwitterAccount = verify_credentials.get("email").toString();
            String username = verify_credentials.get("name").toString();

            if (emailTwitterAccount == null || username == null)
                throw new OAuthServiceException(ApplicationExceptionCode.TWITTER_SERVICE_ERROR);
            Optional<User> userExists = this.userService.findUserByEmail(emailTwitterAccount);

            String token = "";
            User user;
            if (userExists.isPresent()) {
                token = this.tokenService.generateNewToken(userExists.get());
                user = userExists.get();
            } else {
                UserRequestDTO userRequestDTO = new UserRequestDTO();
                userRequestDTO.setUseremail(emailTwitterAccount);
                userRequestDTO.setUsername(username);
                userRequestDTO.setUsergender(null);
                userRequestDTO.setUserpass(null);
                userRequestDTO.setUsernative(false);
                userRequestDTO.setUsersurname(null);
                userRequestDTO.setUserImage(null);
                userRequestDTO.setUserphonenumber(null);
                userRequestDTO.setUserpassrepeat(null);
                user = this.userService.createUser(userRequestDTO);
                token = this.tokenService.generateNewToken(user);
            }
            loginResponseDTO.setUser(user);
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } catch (Exception e) {
            throw new OAuthServiceException(ApplicationExceptionCode.TWITTER_SERVICE_ERROR);
        }
    }

    @GetMapping("/api/loginFacebook")
    public RedirectView loginFacebook() throws Exception {
        URL url = this.facebookService.getFacebookURL();
        return new RedirectView(url.toString());
    }

    @GetMapping("/api/auth/facebook/oauth2callback/")
    public LoginResponseDTO facebookAuthCallback(@RequestParam String code) {
        try {
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            String accessToken = facebookService.getAccessToken(code);
            Map<String, Object> facebookData = facebookService.getData(accessToken);
            String emailFacebookAccount = (String) facebookData.get("email");
            String usernameFacebookAccount = (String) facebookData.get("name");

            System.out.println("email: " + emailFacebookAccount);
            System.out.println("username: " + usernameFacebookAccount);

            if (emailFacebookAccount == null || usernameFacebookAccount == null) throw new Exception();
            Optional<User> userExists = this.userService.findUserByEmail(emailFacebookAccount);

            String token = "";
            User user;
            if (userExists.isPresent()) {
                token = this.tokenService.generateNewToken(userExists.get());
                user = userExists.get();
            } else {
                UserRequestDTO userRequestDTO = new UserRequestDTO();
                userRequestDTO.setUseremail(emailFacebookAccount);
                userRequestDTO.setUsername(usernameFacebookAccount);
                userRequestDTO.setUsergender(null);
                userRequestDTO.setUserpass(null);
                userRequestDTO.setUsernative(false);
                userRequestDTO.setUsersurname(null);
                userRequestDTO.setUserImage(null);
                userRequestDTO.setUserphonenumber(null);
                userRequestDTO.setUserpassrepeat(null);
                user = this.userService.createUser(userRequestDTO);
                token = this.tokenService.generateNewToken(user);
            }
            loginResponseDTO.setUser(user);
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } catch (Exception e) {
            throw new OAuthServiceException(ApplicationExceptionCode.FACEBOOK_SERVICE_ERRROR);
        }
    }
}
