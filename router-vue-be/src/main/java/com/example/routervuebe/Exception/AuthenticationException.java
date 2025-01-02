package com.example.routervuebe.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends RuntimeException {
    private final MessageError messageError;
    private final HttpStatus httpStatus;

    public AuthenticationException(MessageError messageError, HttpStatus httpStatus) {
        super(messageError.getMessage());
        this.messageError = messageError;
        this.httpStatus = httpStatus;
    }

}



