package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayContainer;
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

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Service
public class AuthServiceStep {

    @Autowired
    GatewayContainer gatewayContainer;

    @Autowired
    AuthService authService;

    //Срок действия OTP в миллисекундах
    public final int otpTimer = 60000;

    @Step("Step | Полная авторизация зарегистрированного пользователя")
    public ResponseSignInVerify signInE2eStep(User user) {
        log.info("Step | Полная авторизация зарегистрированного пользователя");
        return signInVerifyStep(new RequestSignInVerify(
                user.getDeviceId(),
                signInStep(user).getData().getConfirmationKey(),
                user.getOtp()));
    }

    @Step("Step | Авторизация зарегистрированного пользователя")
    public ResponseSignIn signInStep(User user) {
        log.info("Step | Авторизация зарегистрированного пользователя");
        ResponseSignIn response = authService.postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId())
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignIn.class);

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(response.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(response.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response;
    }

    @Step("Step | Авторизация НЕ зарегистрированного пользователя")
    public void signInInvalidPhoneStep(User user) {
        log.info("Step | Авторизация НЕ зарегистрированного пользователя");
        ResponseSignIn response = authService.postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId())
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignIn.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке в ответе");
        assertNotNull(response.getErrorMessage(),
                "В ответе пустой errorMessage при ошибке");
    }

    @Step("Step | Авторизация с НЕверным паролем")
    public void signInInvalidPasswordStep(User user, String password) {
        log.info("Step | Авторизация зарегистрированного пользователя с НЕверным паролем");
        ResponseSignIn response = authService.postSignIn(
                        user.getPhoneNumber(), password, user.getDeviceId())
                .statusCode(SC_FORBIDDEN)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignIn.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке в ответе");
        assertNotNull(response.getErrorMessage(),
                "В ответе пустой errorMessage при ошибке");
    }

    @Step("Step | Верификация с верным СМС кодом")
    public ResponseSignInVerify signInVerifyStep(RequestSignInVerify requestSignInVerify) {
        log.info("Step | Верификация с верным СМС кодом");
        ResponseSignInVerify response = authService.postSignInVerify(requestSignInVerify)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignInVerify.class);

        switch (gatewayContainer.getUser().getRole()) {
            case "admin": gatewayContainer.setAdminAccessToken(response.getData().getAccessToken());
            case "user": gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
        }

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(response.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(response.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(response.getData().getAccessTokenType(),
                "В поле accessTokenType null");

        return response;
    }

    @Step("Step | Верификация с неверным СМС кодом")
    public void signInVerifyInvalidOtpStep(RequestSignInVerify requestSignInVerify) {
        log.info("Step | Верификация с неверным СМС кодом");
        ResponseSignInVerify response = authService.postSignInVerify(requestSignInVerify)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignInVerify.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertNotNull(response.getErrorMessage(),
                "В ответе нет errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signInVerifyOtpExpiredStep(RequestSignInVerify requestSignInVerify) {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        ResponseSignInVerify response = authService.postSignInVerify(requestSignInVerify)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignInVerify.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertEquals(response.getErrorMessage(), ErrorMessage.EXPIRED_OTP.getMessage(),
                "В ответе неверный errorMessage");
    }

    @Step("Step | Регистрация нового пользователя")
    public ResponseSignUp signUpStep(RequestSignUp requestSignUp) {
        log.info("Step | Регистрация нового пользователя");
        ResponseSignUp response = authService.postSignUp(requestSignUp)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUp.class);

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(response.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(response.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response;
    }

    @Step("Step | Регистрация с зарегистрированным номером телефона")
    public void signUpRegisteredPhoneStep(RequestSignUp requestSignUp) {
        log.info("Step | Регистрация с зарегистрированным номером телефона");
        ResponseSignUp response = authService.postSignUp(requestSignUp)
                .statusCode(SC_CONFLICT)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUp.class);

        assertNull(response.getData(),
                "При ответе с ошибкой - поле data не пустое");
        assertEquals(ErrorMessage.NUMBER_EXISTS.getMessage(), response.getErrorMessage(),
                "При ответе с ошибкой вернулось неверное сообщение в поле errorMessage");
    }

    @Step("Step | Регистрация. Верификация СМС кода")
    public void signUpVerifyStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("Step | Регистрация. Верификация СМС кода");
        authService.postSignUpVerify(requestSignUpVerify)
                .statusCode(SC_OK);
    }

    @Step("Step | Регистрация с НЕверным СМС кодом")
    public void signUpVerifyInvalidOtpStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("Step | Регистрация с НЕверным СМС кодом");
        ResponseSignUpVerify response = authService.postSignUpVerify(requestSignUpVerify)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUpVerify.class);

        assertNull(response.getData(),
                "Верификация СМС с ошибкой вернуло НЕ пустое поле data");
        assertEquals(ErrorMessage.INVALID_OTP.getMessage(), response.getErrorMessage(),
                "Верификация СМС с ошибкой вернуло НЕверное errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signUpVerifyExpiredOtpStep(RequestSignUpVerify requestSignUpVerify) {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        ResponseSignUpVerify response = authService.postSignUpVerify(requestSignUpVerify)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUpVerify.class);

        assertNull(response.getData(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула не пустое поле data");
        assertEquals(ErrorMessage.EXPIRED_OTP.getMessage(), response.getErrorMessage(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула неверное errorMessage");
    }

    @Step("Step | Регистрация. Установка пароля")
    public ResponseSignUpSetPassword signUpSetPasswordStep(RequestSignUpSetPassoword requestSignUpSetPassoword) {
        log.info("Step | Регистрация. Установка пароля");
        ResponseSignUpSetPassword response = authService.postSignUpSetPassword(requestSignUpSetPassoword)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUpSetPassword.class);

        switch (gatewayContainer.getUser().getRole()) {
            case "admin": gatewayContainer.setAdminAccessToken(response.getData().getAccessToken());
            case "user": gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
        }
        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(response.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(response.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(response.getData().getAccessTokenType(),
                "В поле accessTokenType null");
        return response;
    }

    @Step("Step | Регистрация с неверным паролем")
    public void signUpInvalidPasswordStep(
            RequestSignUpSetPassoword requestSignUpSetPassoword, String error) {
        log.info("Step | Регистрация с неверным паролем");
        ResponseSignUpSetPassword response = authService.postSignUpSetPassword(requestSignUpSetPassoword)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignUpSetPassword.class);

        assertNull(response.getData(),
                "При ошибке в ответе поле data не пустое");
        assertEquals(error, response.getErrorMessage(),
                "При неверном пароле в ответе вернулся неверный errorMessage");
    }
}
