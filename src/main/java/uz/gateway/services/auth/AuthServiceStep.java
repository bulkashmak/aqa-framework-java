package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayContainer;
import uz.gateway.dto.GatewayResponse;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordSetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordVerifyRequest;
import uz.gateway.dto.auth.resetPassword.response.ResetPasswordResponse;
import uz.gateway.dto.auth.signIn.request.SignInVerifyRequest;
import uz.gateway.dto.auth.signIn.response.SignInResponse;
import uz.gateway.dto.auth.signIn.response.SignInVerifyResponse;
import uz.gateway.dto.auth.signUp.request.SignUpRequest;
import uz.gateway.dto.auth.signUp.request.SignUpSetPasswordRequest;
import uz.gateway.dto.auth.signUp.request.SignUpVerifyRequest;
import uz.gateway.dto.auth.signUp.response.SignUpResponse;
import uz.gateway.dto.auth.signUp.response.SignUpSetPasswordResponse;
import uz.gateway.dto.auth.signUp.response.SignUpVerifyResponse;
import uz.gateway.dto.users.admin.users.response.GetUsersResponse;
import uz.gateway.services.auth.enums.ErrorMessage;
import uz.gateway.services.users.UsersServiceStep;
import uz.gateway.testdata.pojo.User;

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Service
public class AuthServiceStep {

    //    @Autowired
    GatewayContainer gatewayContainer;

    //    @Autowired
    AuthService authService;

    //    @Autowired
    UsersServiceStep usersServiceStep;

    public AuthServiceStep(GatewayContainer gatewayContainer, AuthService authService, @Lazy UsersServiceStep usersServiceStep) {
        this.gatewayContainer = gatewayContainer;
        this.authService = authService;
        this.usersServiceStep = usersServiceStep;
    }

    //Срок действия OTP в миллисекундах
    public final int otpTimer = 60000;

    @Step("Step | Полная авторизация зарегистрированного пользователя")
    public SignInVerifyResponse signInE2eStep(User user) {
        log.info("Step | Полная авторизация зарегистрированного пользователя");
        return signInVerifyStep(new SignInVerifyRequest(
                user.getDeviceId(),
                signInStep(user).getData().getConfirmationKey(),
                user.getOtp()));
    }

    @Step("Step | Авторизация зарегистрированного пользователя")
    public SignInResponse signInStep(User user) {
        log.info("Step | Авторизация зарегистрированного пользователя");
        SignInResponse response = authService.postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId())
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInResponse.class);

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
//        ResponseSignIn response =
        authService.postSignIn(
                        user.getPhoneNumber(), user.getPassword(), user.getDeviceId())
                .statusCode(SC_FORBIDDEN);
//                .contentType(ContentType.JSON)
//                .extract().as(ResponseSignIn.class);
//
//        assertNull(response.getData(),
//                "В поле data НЕ null при ошибке в ответе");
//        assertNotNull(response.getErrorMessage(),
//                "В ответе пустой errorMessage при ошибке");
    }

    @Step("Step | Авторизация с НЕверным паролем")
    public void signInInvalidPasswordStep(User user, String password) {
        log.info("Step | Авторизация зарегистрированного пользователя с НЕверным паролем");
//        ResponseSignIn response =
        authService.postSignIn(
                        user.getPhoneNumber(), password, user.getDeviceId())
                .statusCode(SC_FORBIDDEN);
//                .contentType(ContentType.JSON)
//                .extract().as(ResponseSignIn.class);
//
//        assertNull(response.getData(),
//                "В поле data НЕ null при ошибке в ответе");
//        assertNotNull(response.getErrorMessage(),
//                "В ответе пустой errorMessage при ошибке");
    }

    @Step("Step | Верификация с верным СМС кодом")
    public SignInVerifyResponse signInVerifyStep(SignInVerifyRequest signInVerifyRequest) {
        log.info("Step | Верификация с верным СМС кодом");
        SignInVerifyResponse response = authService.postSignInVerify(signInVerifyRequest)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        switch (gatewayContainer.getUser().getRole()) {
            case "admin":
                gatewayContainer.setAdminAccessToken(response.getData().getAccessToken());
            case "user":
                gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
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
    public void signInVerifyInvalidOtpStep(SignInVerifyRequest signInVerifyRequest) {
        log.info("Step | Верификация с неверным СМС кодом");
        SignInVerifyResponse response = authService.postSignInVerify(signInVerifyRequest)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertNotNull(response.getErrorMessage(),
                "В ответе нет errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signInVerifyOtpExpiredStep(SignInVerifyRequest signInVerifyRequest) {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        SignInVerifyResponse response = authService.postSignInVerify(signInVerifyRequest)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertEquals(response.getErrorMessage(), ErrorMessage.EXPIRED_OTP.getMessage(),
                "В ответе неверный errorMessage");
    }

    @Step("Step | Регистрация нового пользователя")
    public SignUpResponse signUpStep(SignUpRequest signUpRequest) {
        log.info("Step | Регистрация нового пользователя");
        SignUpResponse response = authService.postSignUp(signUpRequest)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignUpResponse.class);

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(response.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(response.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response;
    }

    @Step("Step | Регистрация с зарегистрированным номером телефона")
    public void signUpRegisteredPhoneStep(SignUpRequest signUpRequest) {
        log.info("Step | Регистрация с зарегистрированным номером телефона");
        SignUpResponse response = authService.postSignUp(signUpRequest)
                .statusCode(SC_CONFLICT)
                .contentType(ContentType.JSON)
                .extract().as(SignUpResponse.class);

        assertNull(response.getData(),
                "При ответе с ошибкой - поле data не пустое");
        assertEquals(ErrorMessage.NUMBER_EXISTS.getMessage(), response.getErrorMessage(),
                "При ответе с ошибкой вернулось неверное сообщение в поле errorMessage");
    }

    @Step("Step | Регистрация. Верификация СМС кода")
    public void signUpVerifyStep(SignUpVerifyRequest signUpVerifyRequest) {
        log.info("Step | Регистрация. Верификация СМС кода");
        authService.postSignUpVerify(signUpVerifyRequest)
                .statusCode(SC_OK);
    }

    @Step("Step | Регистрация с НЕверным СМС кодом")
    public void signUpVerifyInvalidOtpStep(SignUpVerifyRequest signUpVerifyRequest) {
        log.info("Step | Регистрация с НЕверным СМС кодом");
        SignUpVerifyResponse response = authService.postSignUpVerify(signUpVerifyRequest)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignUpVerifyResponse.class);

        assertNull(response.getData(),
                "Верификация СМС с ошибкой вернуло НЕ пустое поле data");
        assertEquals(ErrorMessage.INCOMPATIBLE_OTP.getMessage(), response.getErrorMessage(),
                "Верификация СМС с ошибкой вернуло НЕверное errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signUpVerifyExpiredOtpStep(SignUpVerifyRequest signUpVerifyRequest) {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        SignUpVerifyResponse response = authService.postSignUpVerify(signUpVerifyRequest)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignUpVerifyResponse.class);

        assertNull(response.getData(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула не пустое поле data");
        assertEquals(ErrorMessage.EXPIRED_OTP.getMessage(), response.getErrorMessage(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула неверное errorMessage");
    }

    @Step("Step | Регистрация. Установка пароля")
    public SignUpSetPasswordResponse signUpSetPasswordStep(SignUpSetPasswordRequest signUpSetPasswordRequest) {
        log.info("Step | Регистрация. Установка пароля");
        SignUpSetPasswordResponse response = authService.postSignUpSetPassword(signUpSetPasswordRequest)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignUpSetPasswordResponse.class);

        switch (gatewayContainer.getUser().getRole()) {
            case "admin":
                gatewayContainer.setAdminAccessToken(response.getData().getAccessToken());
            case "user":
                gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
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
            SignUpSetPasswordRequest signUpSetPasswordRequest, String error) {
        log.info("Step | Регистрация с неверным паролем");
        SignUpSetPasswordResponse response = authService.postSignUpSetPassword(signUpSetPasswordRequest)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignUpSetPasswordResponse.class);

        assertNull(response.getData(),
                "При ошибке в ответе поле data не пустое");
        assertEquals(error, response.getErrorMessage(),
                "При неверном пароле в ответе вернулся неверный errorMessage");
    }

    @Step("Step | Полный сброс паролял")
    public void resetPasswordE2eStep(User user, String newPassword) {
        log.info("Step | Полный сброс паролял");
        ResetPasswordResponse resetPasswordResponse = resetPasswordStep(new ResetPasswordRequest(
                user.getPhoneNumber(), "captcha"));
        resetPasswordVerifyStep(new ResetPasswordVerifyRequest(
                resetPasswordResponse.getData().getConfirmationKey(), user.getOtp()));
        resetPasswordSetPasswordStep(new ResetPasswordSetPasswordRequest(
                resetPasswordResponse.getData().getConfirmationKey(), newPassword));
    }

    @Step("Step | Сброс пароля зарегистрированного пользователя")
    public ResetPasswordResponse resetPasswordStep(ResetPasswordRequest requestBody) {
        log.info("Step | Сброс пароля зарегистрированного пользователя");
        ResetPasswordResponse response = authService.postResetPassword(requestBody)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResetPasswordResponse.class);

        assertNull(response.getErrorMessage(),
                "При сбросе пароля вернулся errorMessage");
        assertNotNull(response.getData(),
                "При сбросе пароля вернулся пустой data");
        assertNotNull(response.getData().getConfirmationKey(),
                "При сбросе пароля вернулся пустой confirmationKey");

        return response;
    }

    @Step("Step | Сброс пароля НЕзарегистрированного пользователя")
    public ResetPasswordResponse resetPasswordInvalidPhoneStep(ResetPasswordRequest requestBody) {
        log.info("Step | Сброс пароля НЕзарегистрированного пользователя");
        ResetPasswordResponse response = authService.postResetPassword(requestBody)
                .statusCode(SC_NOT_FOUND)
                .contentType(ContentType.JSON)
                .extract().as(ResetPasswordResponse.class);

        assertNull(response.getData(),
                "Сброс пароля. Незарегистрированный номер телефона, data не null");
        assertEquals(ErrorMessage.USER_NOT_FOUND_BY_PHONE.getMessage(), response.getErrorMessage(),
                "Сброс пароля. Незарегистрированный номер телефона. Неверный errorMessage");

        return response;
    }

    @Step("Step | Сброс пароля. Верификация СМС кода")
    public void resetPasswordVerifyStep(ResetPasswordVerifyRequest requestBody) {
        log.info("Step | Сброс пароля. Верификация СМС кода");
        authService.postResetPasswordVerify(requestBody)
                .statusCode(SC_OK);
    }

    @Step("Step | Сброс пароля с неверный СМС кодом")
    public GatewayResponse resetPasswordVerifyInvalidOtpStep(ResetPasswordVerifyRequest requestBody) {
        log.info("Step | Сброс пароля с неверный СМС кодом");
        GatewayResponse response = authService.postResetPasswordVerify(requestBody)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(GatewayResponse.class);

        assertNull(response.getData(),
                "Сброс пароля. Неверный СМС код. Поле data не null");
        assertEquals(ErrorMessage.INCOMPATIBLE_OTP.getMessage(), response.getErrorMessage(),
                "Сброс пароля. Неверный СМС код. Неверный errorMessage");

        return response;
    }

    @Step("Step | Сброс пароля. Неверная длина СМС кода")
    public GatewayResponse resetPasswordVerifyInvalidLengthOtpStep(ResetPasswordVerifyRequest requestBody) {
        log.info("Step | Сброс пароля. Неверная длина СМС кода");
        GatewayResponse response = authService.postResetPasswordVerify(requestBody)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(GatewayResponse.class);

        assertNull(response.getData(),
                "Сброс пароля. Неверный СМС код. Поле data не null");
        assertEquals(ErrorMessage.INVALID_OTP.getMessage(), response.getErrorMessage(),
                "Сброс пароля. Неверный СМС код. Неверный errorMessage");

        return response;
    }

    @Step("Step | Сброс пароля. Установка нового пароля")
    public void resetPasswordSetPasswordStep(ResetPasswordSetPasswordRequest requestBody) {
        log.info("Step | Сброс пароля. Установка нового пароля");
        authService.postResetPasswordSetPassword(requestBody)
                .statusCode(SC_OK);
    }

    @Step("Step | Сброс пароля. Неверный пароль")
    public GatewayResponse resetPasswordInvalidPasswordStep(
            ResetPasswordSetPasswordRequest requestBody, String error) {
        log.info("Step | Сброс пароля. Неверный пароль");
        GatewayResponse response = authService.postResetPasswordSetPassword(requestBody)
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(GatewayResponse.class);

        assertNull(response.getData(),
                "Сброс пароля. Неверный пароль. Поле data не null");
        assertEquals(error, response.getErrorMessage());

        return response;
    }

    @Step("Precondition | Регистрация. Удаление пользователя по номеру телефона")
    public void deleteUserByPhonePrecondition(String phoneNumber, User admin) {
        log.info("Precondition | Удаление пользователя по номеру телефона");
        signInE2eStep(admin);
        GetUsersResponse getUsersResponse = usersServiceStep.getUsersStep();
        usersServiceStep.deleteUserByPhone(phoneNumber, getUsersResponse);
    }

    @Step("Precondition | Сброс пароля")
    public void resetPasswordPrecondition(User user, String newPassword) {
        log.info("Precondition | Сброс пароля");
        resetPasswordE2eStep(user, newPassword);
        signInInvalidPasswordStep(user, user.getPassword());
    }
}
