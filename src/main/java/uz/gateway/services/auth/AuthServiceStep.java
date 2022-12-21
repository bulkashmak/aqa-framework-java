package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.dto.auth.signUp.request.RequestSignUp;
import uz.gateway.dto.auth.signUp.request.RequestSignUpSetPassoword;
import uz.gateway.dto.auth.signUp.request.RequestSignUpVerify;
import uz.gateway.dto.auth.signUp.response.ResponseSignUp;
import uz.gateway.dto.auth.signUp.response.ResponseSignUpSetPassword;
import uz.gateway.testdata.pojo.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class AuthServiceStep extends AuthService {

    AuthServiceCheck authServiceCheck = new AuthServiceCheck();

    @Step("[ШАГ] Авторизация зарегистрированного пользователя")
    public ResponseSignIn signInStep(int expectedStatusCode, User user) {
        log.info("[ШАГ] Авторизация зарегистрированного пользователя");
        Response response = postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId());

        assertEquals(expectedStatusCode, response.getStatusCode());
        authServiceCheck.signInStepCheck(response.getBody().as(ResponseSignIn.class));

        return response.getBody().as(ResponseSignIn.class);
    }

    @Step("[ШАГ] Авторизация. Верификация СМС кода")
    public ResponseSignInVerify signInVerifyStep(int expectedStatusCode, RequestSignInVerify requestSignInVerify) {
        log.info("[ШАГ] Авторизация. Верификация СМС кода");
        Response response = postSignInVerify(requestSignInVerify);

        assertEquals(expectedStatusCode, response.getStatusCode());
        authServiceCheck.signInVerifyCheck(response.getBody().as(ResponseSignInVerify.class));

        return response.getBody().as(ResponseSignInVerify.class);
    }

    @Step("[ШАГ] Регистрация нового пользователя")
    public ResponseSignUp signUpStep(int expectedStatusCode, RequestSignUp requestSignUp) {
        log.info("[ШАГ] Регистрация нового пользователя");
        Response response = postSignUp(requestSignUp);

        assertEquals(expectedStatusCode, response.getStatusCode());
        authServiceCheck.signUpCheck(response.getBody().as(ResponseSignUp.class));

        return response.getBody().as(ResponseSignUp.class);
    }

    @Step("[ШАГ] Регистрация. Верификация СМС кода")
    public void signUpVerifyStep(int expectedStatusCode, RequestSignUpVerify requestSignUpVerify) {
        log.info("[ШАГ] Регистрация. Верификация СМС кода");
        Response response = postSignUpVerify(requestSignUpVerify);

        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Step("[ШАГ] Регистрация. Установка пароля")
    public ResponseSignUpSetPassword signUpSetPasswordStep(
            int expectedStatusCode, RequestSignUpSetPassoword requestSignUpSetPassoword) {
        log.info("[ШАГ] Регистрация. Установка пароля");
        Response response = postSignUpSetPassword(requestSignUpSetPassoword);

        assertEquals(expectedStatusCode, response.getStatusCode());
        authServiceCheck.signUpSetPasswordCheck(response.getBody().as(ResponseSignUpSetPassword.class));

        return response.getBody().as(ResponseSignUpSetPassword.class);
    }
}
