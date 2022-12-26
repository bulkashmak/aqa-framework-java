package uz.gateway.services.auth.enums;

public enum ErrorMessage {
    EXPIRED_OTP("error.expired.otp"),
    NUMBER_EXISTS("user_with_such_phone_number_is_already_exists"),
    INVALID_OTP("error.not_compatible.otp"),
    USER_NOT_FOUND_BY_PHONE("error.not_found.user_by_phone_number");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
