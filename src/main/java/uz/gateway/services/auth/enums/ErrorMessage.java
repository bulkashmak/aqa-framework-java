package uz.gateway.services.auth.enums;

public enum ErrorMessage {
    EXPIRED_OTP("error.expired.otp"),
    NUMBER_EXISTS("user_with_such_phone_number_is_already_exists"),
    INVALID_OTP("error.not_compatible.otp");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
