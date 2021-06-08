package app.preciojusto.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class OAuthServiceException extends RuntimeException{
    public OAuthServiceException() { }

    public OAuthServiceException(ApplicationExceptionCode code) {
        super(Integer.toString(code.getCode()));
    }
}
