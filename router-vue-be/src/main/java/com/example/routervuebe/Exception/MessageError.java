package com.example.routervuebe.Exception;

public enum MessageError {
    INVALID_USERNAME_PASSWORD("Invalid username or password."),
    USER_NULL("User is null."),
    INVALID_EXPIRED_TOKEN("Invalid expired token."),
    INTERNAL_SERVER_ERROR("An internal server error occurred");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
