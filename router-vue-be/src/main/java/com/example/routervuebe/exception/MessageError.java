package com.example.routervuebe.exception;

public enum MessageError {
    INVALID_USERNAME_PASSWORD("Invalid username or password."),
    USER_NULL("Please fill in the token."),
    INVALID_EXPIRED_TOKEN("Invalid or expired token."),
    OTP_NULL("OTP is null."),
    INVALID_EXPIRED_OTP("Invalid or expired otp."),
    MISSING_TOKEN("Missing token."),
    USERNAME_VALIDATE("Username is already in use."),
    EMAIL_VALIDATE("Email is already in use."),
    VALIDATE_PASSWORD("Password must contain at least one letter, one number, and one special character."),
    LEAVE_GROUP("Successfully left the group."),
    DELETE_MESSAGE("Delete conversation success."),
    USER_NOT_FOUND("User not found."),
    CONVERSATION_NOT_FOUND("Conversation not found."),
    SUCCESS("Success."),
    INTERNAL_SERVER_ERROR("An internal server error occurred");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
