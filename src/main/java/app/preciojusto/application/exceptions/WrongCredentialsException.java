package app.preciojusto.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongCredentialsException extends RuntimeException{
    public WrongCredentialsException() {
    }

    public WrongCredentialsException(ApplicationExceptionCode code) {
        super(Integer.toString(code.getCode()));
    }
}
