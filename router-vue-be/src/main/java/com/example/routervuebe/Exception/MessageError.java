package com.example.routervuebe.Exception;

public enum MessageError {
    INVALID_USERNAME_PASSWORD("Invalid username or password."),
    USER_NULL("Please fill in the token."),
    INVALID_EXPIRED_TOKEN("Invalid or expired token."),
    OTP_NULL("OTP is null."),
    INVALID_EXPIRED_OTP("Invalid or expired otp."),
    INTERNAL_SERVER_ERROR("An internal server error occurred");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
