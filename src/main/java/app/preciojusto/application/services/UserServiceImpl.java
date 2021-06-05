package app.preciojusto.application.services;

import app.preciojusto.application.dto.LoginRequestDTO;
import app.preciojusto.application.dto.LoginResponseDTO;
import app.preciojusto.application.dto.UserRequestDTO;
import app.preciojusto.application.entities.User;
import app.preciojusto.application.entities.UserImage;
import app.preciojusto.application.exceptions.*;
import app.preciojusto.application.repositories.UserImageRepository;
import app.preciojusto.application.repositories.UserRepository;
import app.preciojusto.application.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageService userImageService;

    @Autowired
    private TokenService tokenService;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findUserByUseremailEquals(email);
    }

    @Override
    public Optional<User> findById(Long userid) {
        return this.userRepository.findById(userid);
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO requestUser) {

        LoginResponseDTO result = new LoginResponseDTO();
        this.userRepository.findUserByUseremailEquals(requestUser.getUseremail()).ifPresentOrElse(
                (User user) -> {
                    String storedPassword = user.getUserpass();
                    boolean status;
                    try {
                        status = HashUtil.validatePassword(requestUser.getUserpass(), storedPassword);
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                    if (!status) throw new WrongCredentialsException(ApplicationExceptionCode.WRONG_PASSWORD_ERROR);

                    //correct user and password
                    String token = tokenService.generateNewToken(this.userRepository.findUserByUseremailEquals(requestUser.getUseremail()).orElseThrow(RuntimeException::new));
                    result.setUser(this.userRepository.findUserByUseremailEquals(requestUser.getUseremail()).orElseThrow(RuntimeException::new));
                    result.setToken(token);
                },
                () -> {
                    throw new WrongCredentialsException(ApplicationExceptionCode.WRONG_EMAIL_ERROR);
                });
        return result;
    }

    @Override
    public User createUser(UserRequestDTO request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = new User();
        String generatedSecuredPassword = HashUtil.generatePasswordHash(request.getUserpass());
        user.setUsername(request.getUsername());
        user.setUsersurname(request.getUsersurname());
        user.setUseremail(request.getUseremail());
        user.setUserImage(null);
        user.setUsernative(true); //Check in the future if it is user native or not
        user.setUseractive(true);
        user.setUserphonenumber(null);
        user.setUsergender(request.getUsergender());
        user.setUserpass(generatedSecuredPassword);

        try {
            return this.userRepository.save(user);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.USER_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public User updateUser(UserRequestDTO request) throws ResourceNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
        Charset charset = StandardCharsets.US_ASCII;

        User user = this.findById(request.getUserid())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));

        if (request.getUserImage() != null) {

            if (user.getUserImage() != null) {
                this.userImageService.delete(user.getUserImage().getUsimid());
            }
            UserImage newImage = new UserImage();
            newImage.setUsimname(generateAlphanumericString());
            newImage.setUsimimage(charset.encode(String.valueOf(request.getUserImage())).array());
            newImage.setUser(user);
            user.setUserImage(this.userImageService.save(newImage));
        }

        if (request.getUserpass() != null) {
            String generatedSecuredPassword = HashUtil.generatePasswordHash(request.getUserpass());
            user.setUserpass(generatedSecuredPassword);
        }
        user.setUsername(request.getUsername());
        user.setUsersurname(request.getUsersurname());
        user.setUseremail(request.getUseremail());
        user.setUserphonenumber(request.getUserphonenumber());
        user.setUsergender(request.getUsergender());

        try {
            return this.userRepository.save(user);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.USER_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public User disableUser(UserRequestDTO request) throws ResourceNotFoundException {
        User user = this.findById(request.getUserid())
                .orElseThrow(() -> new ResourceNotFoundException(ApplicationExceptionCode.USER_NOT_FOUND_ERROR));
        user.setUseractive(false);

        try {
            return this.userRepository.save(user);
        } catch (Exception e) {
            throw new ResourceAlreadyExistsException(ApplicationExceptionCode.USER_ALREADY_EXISTS_ERROR);
        }
    }

    @Override
    public void checkRegister(UserRequestDTO request) throws ResourceNotFoundException {
        if (!this.isEmailValid(request.getUseremail()))
            throw new ValidationException(ApplicationExceptionCode.EMAIL_VALIDATION_ERROR);
        if (this.userRepository.findUserByUseremailEquals(request.getUseremail()).isPresent())
            throw new ValidationException(ApplicationExceptionCode.EMAILEXISTS_VALIDATION_ERROR);
        if (!this.isUsernameValid(request.getUsername()))
            throw new ValidationException(ApplicationExceptionCode.USER_VALIDATION_ERROR);
        if (!this.isPhoneNumberValid(request.getUserphonenumber()))
            throw new ValidationException(ApplicationExceptionCode.PHONENUMBER_VALIDATION_ERROR);
        if (!this.isPasswordValid(request.getUserpass()))
            throw new ValidationException(ApplicationExceptionCode.PASSWORD_VALIDATION_ERROR);
        if (!request.getUserpass().equals(request.getRepeatuserpass()))
            throw new ValidationException(ApplicationExceptionCode.SAMEPASSWORD_VALIDATION_ERROR);
    }

    @Override
    public void checkUpdateProfile(UserRequestDTO request, String emailAuth) {

        String requestedEmail = "";
        Optional<User> requestedUser = this.userRepository.findUserByUseremailEquals(request.getUseremail());
        if (requestedUser.isPresent()) {
            requestedEmail = requestedUser.get().getUseremail();
        }

        if (!this.isEmailValid(request.getUseremail()))
            throw new ValidationException(ApplicationExceptionCode.EMAIL_VALIDATION_ERROR);
        if (this.userRepository.findUserByUseremailEquals(request.getUseremail()).isPresent() && !requestedEmail.equals(emailAuth))
            throw new ValidationException(ApplicationExceptionCode.EMAILEXISTS_VALIDATION_ERROR);
        if (!this.isUsernameValid(request.getUsername()))
            throw new ValidationException(ApplicationExceptionCode.USER_VALIDATION_ERROR);
        if (!this.isPhoneNumberValid(request.getUserphonenumber()))
            throw new ValidationException(ApplicationExceptionCode.PHONENUMBER_VALIDATION_ERROR);
        if (!this.isPasswordValid(request.getUserpass()))
            throw new ValidationException(ApplicationExceptionCode.PASSWORD_VALIDATION_ERROR);
        if (!request.getUserpass().equals(request.getRepeatuserpass()))
            throw new ValidationException(ApplicationExceptionCode.SAMEPASSWORD_VALIDATION_ERROR);
    }


    private Boolean isPasswordValid(String password) {
        Pattern passPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@?#$%^&+=])(?=\\S+$).{8,}$");
        Matcher passMatcher = passPattern.matcher(password);
        return passMatcher.find();
    }


    private Boolean isUsernameValid(String username) {
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9._-]{3,}$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        return usernameMatcher.find();
    }

    private Boolean isPhoneNumberValid(String phoneNumber) {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    private Boolean isEmailValid(String email) throws ResourceNotFoundException {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.find();
    }

    private String generateAlphanumericString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();
        String generatedString = null;

        while (generatedString == null) {
            generatedString = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            if (this.userImageService.existsByUsimname(generatedString)) generatedString = null;
        }

        return generatedString;
    }
}
