package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import uz.gateway.GatewayClient;
import uz.gateway.GatewayContext;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signUp.request.RequestSignUpSetPassoword;
import uz.gateway.dto.auth.signUp.request.RequestSignUp;
import uz.gateway.dto.auth.signUp.request.RequestSignUpVerify;

import static io.restassured.RestAssured.given;

@Slf4j
public class AuthServiceStep extends GatewayClient {

    private final GatewayContext context = new GatewayContext();

    @Step("[ШАГ] Авторизация зарегистрированного пользователя")
    public ValidatableResponse postSignIn(String phoneNumber, String password, String deviceId) {
        log.info("[ШАГ] Авторизация зарегистрированного пользователя");
        return given()
                .spec(defaultSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("phoneNumber", phoneNumber)
                .formParam("password", password)
                .formParam("deviceId", deviceId)
                .header("lang", "RU")
                .post(Path.SIGN_IN.getPath())
                .then();
    }

    @Step("[ШАГ] Авторизация. Верификация СМС кода")
    public ValidatableResponse postSignInVerify(RequestSignInVerify requestSignInVerify) {
        log.info("[ШАГ] Авторизация. Верификация СМС кода");
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignInVerify)
                .post(Path.SIGN_IN_VERIFY.getPath())
                .then();
    }

    @Step("[ШАГ] Регистрация нового пользователя")
    public ValidatableResponse postSignUp(RequestSignUp requestSignUp) {
        log.info("[ШАГ] Регистрация нового пользователя");
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUp)
                .post(Path.SIGN_UP.getPath())
                .then();
    }

    @Step("[ШАГ] Регистрация. Верификация СМС кода")
    public ValidatableResponse postSignUpVerify(RequestSignUpVerify requestSignUpVerify) {
        log.info("[ШАГ] Регистрация. Верификация СМС кода");
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUpVerify)
                .post(Path.SIGN_UP_VERIFY.getPath())
                .then();
    }

    @Step("[ШАГ] Регистрация. Установка пароля")
    public ValidatableResponse postSignUpSetPassword(RequestSignUpSetPassoword requestSignUpSetPassoword) {
        log.info("[ШАГ] Регистрация. Установка пароля");
        return given()
                .spec(defaultSpec)
                .contentType(ContentType.JSON)
                .body(requestSignUpSetPassoword)
                .post(Path.SIGN_UP_SET_PASSWORD.getPath())
                .then();
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
}
