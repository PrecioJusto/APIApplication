package app.preciojusto.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ValidationException extends RuntimeException{
    public ValidationException() {
    }
    public ValidationException(ApplicationExceptionCode code) {
        super(Integer.toString(code.getCode()));
    }
}
