package uz.gateway.services.auth;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayClient;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordSetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordVerifyRequest;
import uz.gateway.dto.auth.signIn.request.SignInVerifyRequest;
import uz.gateway.dto.auth.signUp.request.SignUpRequest;
import uz.gateway.dto.auth.signUp.request.SignUpSetPasswordRequest;
import uz.gateway.dto.auth.signUp.request.SignUpVerifyRequest;

import static io.restassured.RestAssured.given;


@Slf4j
@Service
public class AuthService extends GatewayClient {

    public ValidatableResponse postSignIn(String phoneNumber, String password, String deviceId) {
        log.info("POST запрос {}", Path.SIGN_IN.getPath());
        return given()
                .spec(defaultSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam(SignInForm.PHONE_NUMBER.getField(), phoneNumber)
                .formParam(SignInForm.PASSWORD.getField(), password)
                .formParam(SignInForm.DEVICE_ID.getField(), deviceId)
                .header("lang", "RU")
                .post(Path.SIGN_IN.getPath())
                .then();
    }

    public ValidatableResponse postSignInVerify(SignInVerifyRequest requestBody) {
        log.info("POST запрос {}", Path.SIGN_IN_VERIFY.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.SIGN_IN_VERIFY.getPath())
                .then();
    }

    public ValidatableResponse postSignUp(SignUpRequest requestBody) {
        log.info("POST запрос {}", Path.SIGN_UP.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.SIGN_UP.getPath())
                .then();
    }

    public ValidatableResponse postSignUpVerify(SignUpVerifyRequest requestBody) {
        log.info("POST запрос {}", Path.SIGN_UP_VERIFY.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.SIGN_UP_VERIFY.getPath())
                .then();
    }

    public ValidatableResponse postSignUpSetPassword(SignUpSetPasswordRequest requestBody) {
        log.info("POST запрос {}", Path.SIGN_UP_SET_PASSWORD.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.SIGN_UP_SET_PASSWORD.getPath())
                .then();
    }

    public ValidatableResponse postResetPassword(ResetPasswordRequest requestBody) {
        log.info("POST запрос {}", Path.RESET_PASSWORD.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.RESET_PASSWORD.getPath())
                .then();
    }

    public ValidatableResponse postResetPasswordVerify(ResetPasswordVerifyRequest requestBody) {
        log.info("POST запрос {}", Path.RESET_PASSWORD_VERIFY.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.RESET_PASSWORD_VERIFY.getPath())
                .then();
    }

    public ValidatableResponse postResetPasswordSetPassword(ResetPasswordSetPasswordRequest requestBody) {
        log.info("POST запрос {}", Path.RESET_PASSWORD_SET_PASSWORD.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(Path.RESET_PASSWORD_SET_PASSWORD.getPath())
                .then();
    }

    private enum Path {
        SIGN_IN("/auth/sign-in"),
        SIGN_IN_VERIFY("/auth/sign-in/verify"),
        SIGN_UP("/auth/sign-up"),
        SIGN_UP_VERIFY("/auth/sign-up/verify"),
        SIGN_UP_SET_PASSWORD("/auth/sign-up/set-password"),
        RESET_PASSWORD("/auth/reset"),
        RESET_PASSWORD_VERIFY("/auth/reset/verify"),
        RESET_PASSWORD_SET_PASSWORD("/auth/reset/set-password");

        private final String path;

        Path(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private enum SignInForm {
        PHONE_NUMBER("phoneNumber"),
        PASSWORD("password"),
        DEVICE_ID("deviceId");

        private final String field;

        SignInForm(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }
}
