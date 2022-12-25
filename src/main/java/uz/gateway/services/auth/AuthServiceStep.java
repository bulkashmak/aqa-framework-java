package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.dto.auth.signUp.request.RequestSignUp;
import uz.gateway.dto.auth.signUp.request.RequestSignUpSetPassoword;
import uz.gateway.dto.auth.signUp.request.RequestSignUpVerify;
import uz.gateway.dto.auth.signUp.response.ResponseSignUp;
import uz.gateway.dto.auth.signUp.response.ResponseSignUpSetPassword;
import uz.gateway.dto.auth.signUp.response.ResponseSignUpVerify;
import uz.gateway.services.auth.enums.ErrorMessage;
import uz.gateway.testdata.pojo.User;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Service
public class AuthServiceStep {

    @Autowired
    AuthService authService;

    //Срок действия OTP в миллисекундах
    public final int otpTimer = 60000;

    @Step("[ШАГ] Авторизация зарегистрированного пользователя")
    public ResponseSignIn signInStep(User user) {
        log.info("[ШАГ] Авторизация зарегистрированного пользователя");
        Response response = authService.postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId());

//        Response responseDemo = postSignIn(
//                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId());
//        responseDemo.then().statusCode(200);

        assertEquals(200, response.getStatusCode(),
                "При авторизации вернулся неверный статус код");
        ResponseSignIn responseSignIn = response.getBody().as(ResponseSignIn.class);
        assertNull(responseSignIn.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(responseSignIn.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(responseSignIn.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return responseSignIn;
    }

    @Step("[ШАГ] Авторизация НЕ зарегистрированного пользователя")
    public void signInInvalidPhoneStep(User user) {
        log.info("[ШАГ] Авторизация НЕ зарегистрированного пользователя");
        Response response = authService.postSignIn(
                user.getPhoneNumber(), user.getPassword(), user.getDeviceId());

        assertEquals(403, response.getStatusCode(),
                "При авторизации с неверным телефоном вернулся неверный статус код");
        ResponseSignIn responseSignIn = response.getBody().as(ResponseSignIn.class);
        assertNull(responseSignIn.getData(),
                "В поле data НЕ null при ошибке в ответе");
        assertNotNull(responseSignIn.getErrorMessage(),
                "В ответе пустой errorMessage при ошибке");
    }

    @Step("[ШАГ] Авторизация с НЕверным паролем")
    public void signInInvalidPasswordStep(User user, String password) {
        log.info("[ШАГ] Авторизация зарегистрированного пользователя с НЕверным паролем");
        Response response = authService.postSignIn(
                user.getPhoneNumber(), password, user.getDeviceId());

        assertEquals(403, response.getStatusCode(),
                "При авторизации с НЕверным паролем вернулся неверный статус код");
        ResponseSignIn responseSignIn = response.getBody().as(ResponseSignIn.class);
        assertNull(responseSignIn.getData(),
                "В поле data НЕ null при ошибке в ответе");
        assertNotNull(responseSignIn.getErrorMessage(),
                "В ответе пустой errorMessage при ошибке");
    }

    @Step("[ШАГ] Верификация с верным СМС кодом")
    public ResponseSignInVerify signInVerifyStep(RequestSignInVerify requestSignInVerify) {
        log.info("[ШАГ] Верификация с верным СМС кодом");
        Response response = authService.postSignInVerify(requestSignInVerify);

        assertEquals(200, response.getStatusCode(),
                String.format("При верификации СМС кода вернулся %s статус код", response.getStatusCode()));
        ResponseSignInVerify responseSignInVerify = response.getBody().as(ResponseSignInVerify.class);
        assertNull(responseSignInVerify.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(responseSignInVerify.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(responseSignInVerify.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(responseSignInVerify.getData().getAccessTokenType(),
                "В поле accessTokenType null");

        return response.getBody().as(ResponseSignInVerify.class);
    }

    @Step("[ШАГ] Верификация с неверным СМС кодом")
    public void signInVerifyInvalidOtpStep(RequestSignInVerify requestSignInVerify) {
        log.info("[ШАГ] Верификация с неверным СМС кодом");
        Response response = authService.postSignInVerify(requestSignInVerify);

        assertEquals(400, response.getStatusCode(),
                String.format("При верификации НЕверного СМС кода вернулся %s статус код", response.getStatusCode()));
        ResponseSignInVerify responseSignInVerify = response.getBody().as(ResponseSignInVerify.class);
        assertNull(responseSignInVerify.getData(),
                "В поле data НЕ null при ошибке");
        assertNotNull(responseSignInVerify.getErrorMessage(),
                "В ответе нет errorMessage");
    }

    @Step("[ШАГ] Верификация с просроченным СМС кодом")
    public void signInVerifyOtpExpiredStep(RequestSignInVerify requestSignInVerify) {
        log.info("[ШАГ] Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        Response response = authService.postSignInVerify(requestSignInVerify);

        assertEquals(400, response.getStatusCode());
        ResponseSignInVerify responseSignInVerify = response.getBody().as(ResponseSignInVerify.class);
        assertNull(responseSignInVerify.getData(),
                "В поле data НЕ null при ошибке");
        assertEquals(responseSignInVerify.getErrorMessage(), ErrorMessage.EXPIRED_OTP.getMessage(),
                "В ответе неверный errorMessage");
    }

    @Step("[ШАГ] Регистрация нового пользователя")
    public ResponseSignUp signUpStep(RequestSignUp requestSignUp) {
        log.info("[ШАГ] Регистрация нового пользователя");
        Response response = authService.postSignUp(requestSignUp);

        assertEquals(200, response.getStatusCode());
        ResponseSignUp responseSignUp = response.getBody().as(ResponseSignUp.class);
        assertNull(responseSignUp.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(responseSignUp.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(responseSignUp.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response.getBody().as(ResponseSignUp.class);
    }

    @Step("[ШАГ] Регистрация с зарегистрированным номером телефона")
    public void signUpRegisteredPhoneStep(RequestSignUp requestSignUp) {
        log.info("[ШАГ] Регистрация с зарегистрированным номером телефона");
        Response response = authService.postSignUp(requestSignUp);

        assertEquals(409, response.getStatusCode(),
                "При регистрации с зарегистрированным телефоном пришел неверный статус код");
        ResponseSignUp responseSignUp = response.getBody().as(ResponseSignUp.class);
        assertNull(responseSignUp.getData(),
                "При ответе с ошибкой - поле data не пустое");
        assertEquals(ErrorMessage.NUMBER_EXISTS.getMessage(), responseSignUp.getErrorMessage(),
                "При ответе с ошибкой вернулось неверное сообщение в поле errorMessage");
    }

    @Step("[ШАГ] Регистрация. Верификация СМС кода")
    public void signUpVerifyStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("[ШАГ] Регистрация. Верификация СМС кода");
        Response response = authService.postSignUpVerify(requestSignUpVerify);

        assertEquals(200, response.getStatusCode());
    }

    @Step("[ШАГ] Регистрация с НЕверным СМС кодом")
    public void signUpVerifyInvalidOtpStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("[ШАГ] Регистрация с НЕверным СМС кодом");
        Response response = authService.postSignUpVerify(requestSignUpVerify);

        assertEquals(400, response.getStatusCode(),
                "Верификация НЕверного СМС кода вернуло неверный статус код");
        ResponseSignUpVerify responseSignUpVerify = response.getBody().as(ResponseSignUpVerify.class);
        assertNull(responseSignUpVerify.getData(),
                "Верификация СМС с ошибкой вернуло НЕ пустое поле data");
        assertEquals(ErrorMessage.INVALID_OTP.getMessage(), responseSignUpVerify.getErrorMessage(),
                "Верификация СМС с ошибкой вернуло НЕверное errorMessage");
    }

    @Step("[ШАГ] Верификация с просроченным СМС кодом")
    public void signUpVerifyExpiredOtpStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("[ШАГ] Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        Response response = authService.postSignUpVerify(requestSignUpVerify);

        assertEquals(400, response.getStatusCode(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула неверный статус код");
        ResponseSignUpVerify responseSignUpVerify = response.getBody().as(ResponseSignUpVerify.class);
        assertNull(responseSignUpVerify.getData(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула не пустое поле data");
        assertEquals(ErrorMessage.EXPIRED_OTP.getMessage(), responseSignUpVerify.getErrorMessage(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула неверное errorMessage");
    }

    @Step("[ШАГ] Регистрация. Установка пароля")
    public ResponseSignUpSetPassword signUpSetPasswordStep(
            int expectedStatusCode, RequestSignUpSetPassoword requestSignUpSetPassoword) {
        log.info("[ШАГ] Регистрация. Установка пароля");
        Response response = authService.postSignUpSetPassword(requestSignUpSetPassoword);

        assertEquals(expectedStatusCode, response.getStatusCode());
        ResponseSignUpSetPassword responseSignUpSetPassword = response.getBody().as(ResponseSignUpSetPassword.class);
        assertNull(responseSignUpSetPassword.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(responseSignUpSetPassword.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(responseSignUpSetPassword.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(responseSignUpSetPassword.getData().getAccessTokenType(),
                "В поле accessTokenType null");
        return response.getBody().as(ResponseSignUpSetPassword.class);
    }

    @Step("[ШАГ] Регистрация с неверным паролем")
    public void signUpInvalidPasswordStep(
            RequestSignUpSetPassoword requestSignUpSetPassoword, String error) {
        log.info("[ШАГ] Регистрация с неверным паролем");
        Response response = authService.postSignUpSetPassword(requestSignUpSetPassoword);

        assertEquals(400, response.getStatusCode(),
                "При ошибке в ответе вернулся неверный статус код");
        ResponseSignUpSetPassword responseSignUpSetPassword = response.getBody().as(ResponseSignUpSetPassword.class);
        assertNull(responseSignUpSetPassword.getData(),
                "При ошибке в ответе поле data не пустое");
        assertEquals(error, responseSignUpSetPassword.getErrorMessage(),
                "При неверном пароле в ответе вернулся неверный errorMessage");
    }
}
