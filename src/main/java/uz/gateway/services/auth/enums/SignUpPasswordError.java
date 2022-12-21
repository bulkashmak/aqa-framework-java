package uz.gateway.services.auth.enums;

public enum SignUpPasswordError {
    NOT_NULL("error.invalid.password.not_null"),
    TOO_SHORT("error.invalid.password.too_short"),
    TOO_LONG("error.invalid.password.too_long"),
    INVALID_CHARACTERS("error.invalid.password.must_not_contain_invalid_character"),
    NO_LETTER("error.invalid.password.must_include_letter"),
    NO_DIGIT("error.invalid.password.must_include_digit");

    private final String error;

    SignUpPasswordError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
