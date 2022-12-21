package uz.gateway.services.auth;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayClient;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signUp.request.RequestSignUp;
import uz.gateway.dto.auth.signUp.request.RequestSignUpSetPassoword;
import uz.gateway.dto.auth.signUp.request.RequestSignUpVerify;

import static io.restassured.RestAssured.given;

@Service
@Slf4j
public class AuthService extends GatewayClient {

    public Response postSignIn(String phoneNumber, String password, String deviceId) {
        log.info("POST запрос {}", Path.SIGN_IN.getPath());
        return given()
                .spec(defaultSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam(SignInForm.PHONE_NUMBER.getField(), phoneNumber)
                .formParam(SignInForm.PASSWORD.getField(), password)
                .formParam(SignInForm.DEVICE_ID.getField(), deviceId)
                .header("lang", "RU")
                .post(Path.SIGN_IN.getPath());
    }

    public Response postSignInVerify(RequestSignInVerify requestSignInVerify) {
        log.info("POST запрос {}", Path.SIGN_IN_VERIFY.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignInVerify)
                .post(Path.SIGN_IN_VERIFY.getPath());
    }

    public Response postSignUp(RequestSignUp requestSignUp) {
        log.info("POST запрос {}", Path.SIGN_UP.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUp)
                .post(Path.SIGN_UP.getPath());
    }

    public Response postSignUpVerify(RequestSignUpVerify requestSignUpVerify) {
        log.info("POST запрос {}", Path.SIGN_UP_VERIFY.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUpVerify)
                .post(Path.SIGN_UP_VERIFY.getPath());
    }

    public Response postSignUpSetPassword(RequestSignUpSetPassoword requestSignUpSetPassoword) {
        log.info("POST запрос {}", Path.SIGN_UP_SET_PASSWORD.getPath());
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUpSetPassoword)
                .post(Path.SIGN_UP_SET_PASSWORD.getPath());
    }

    private enum Path {
        SIGN_IN("/auth/sign-in"),
        SIGN_IN_VERIFY("/auth/sign-in/verify"),
        SIGN_UP("/auth/sign-up"),
        SIGN_UP_VERIFY("/auth/sign-up/verify"),
        SIGN_UP_SET_PASSWORD("/auth/sign-up/set-password");

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
