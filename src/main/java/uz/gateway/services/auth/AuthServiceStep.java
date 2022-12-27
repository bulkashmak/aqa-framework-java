package uz.gateway.services.auth;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayContainer;
import uz.gateway.dto.GatewayResponse;
import uz.gateway.dto.auth.refreshToken.RefreshTokenRequest;
import uz.gateway.dto.auth.refreshToken.RefreshTokenResponse;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordSetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordVerifyRequest;
import uz.gateway.dto.auth.resetPassword.response.ResetPasswordResponse;
import uz.gateway.dto.auth.signIn.request.SignInVerifyRequest;
import uz.gateway.dto.auth.signIn.response.SignInResponse;
import uz.gateway.dto.auth.signIn.response.SignInVerifyResponse;
import uz.gateway.dto.auth.signOut.SignOutRequest;
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

import java.util.Map;

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
    public SignInVerifyResponse signInE2eStep() {
        log.info("Step | Полная авторизация зарегистрированного пользователя");
        signInStep();
        return signInVerifyStep(gatewayContainer.getUser().getOtp());
    }

    @Step("Step | Авторизация зарегистрированного пользователя")
    public SignInResponse signInStep() {
        log.info("Step | Авторизация зарегистрированного пользователя");
        SignInResponse response = authService.postSignIn(
                        gatewayContainer.getUser().getPhoneNumber(),
                        gatewayContainer.getUser().getPassword(),
                        gatewayContainer.getUser().getDeviceId())
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInResponse.class);

        gatewayContainer.setConfirmationKey(response.getData().getConfirmationKey());

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(response.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(response.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response;
    }

    @Step("Step | Авторизация НЕ зарегистрированного пользователя")
    public void signInInvalidPhoneStep(String invalidPhone) {
        log.info("Step | Авторизация НЕ зарегистрированного пользователя");
//        ResponseSignIn response =
        authService.postSignIn(
                        invalidPhone,
                        gatewayContainer.getUser().getPassword(),
                        gatewayContainer.getUser().getDeviceId())
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
    public void signInInvalidPasswordStep(String invalidPassword) {
        log.info("Step | Авторизация зарегистрированного пользователя с НЕверным паролем");
//        ResponseSignIn response =
        authService.postSignIn(
                        gatewayContainer.getUser().getPhoneNumber(),
                        invalidPassword,
                        gatewayContainer.getUser().getDeviceId())
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
    public SignInVerifyResponse signInVerifyStep(String otp) {
        log.info("Step | Верификация с верным СМС кодом");
        SignInVerifyResponse response = authService.postSignInVerify(new SignInVerifyRequest(
                        gatewayContainer.getUser().getDeviceId(),
                        gatewayContainer.getConfirmationKey(),
                        otp))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
        gatewayContainer.setUserRefreshToken(response.getData().getRefreshToken());

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
    public void signInVerifyInvalidOtpStep(String invalidOtp) {
        log.info("Step | Верификация с неверным СМС кодом");
        SignInVerifyResponse response = authService.postSignInVerify(new SignInVerifyRequest(
                        gatewayContainer.getUser().getDeviceId(),
                        gatewayContainer.getConfirmationKey(),
                        invalidOtp))
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertNotNull(response.getErrorMessage(),
                "В ответе нет errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signInVerifyOtpExpiredStep() {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        SignInVerifyResponse response = authService.postSignInVerify(new SignInVerifyRequest(
                        gatewayContainer.getUser().getDeviceId(),
                        gatewayContainer.getConfirmationKey(),
                        gatewayContainer.getUser().getOtp()))
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        assertNull(response.getData(),
                "В поле data НЕ null при ошибке");
        assertEquals(response.getErrorMessage(), ErrorMessage.EXPIRED_OTP.getMessage(),
                "В ответе неверный errorMessage");
    }

    @Step("Step | Регистрация нового пользователя")
    public SignUpResponse signUpStep() {
        log.info("Step | Регистрация нового пользователя");
        SignUpResponse response = authService.postSignUp(new SignUpRequest(
                        gatewayContainer.getUser().getPhoneNumber(),
                        "captcha"))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignUpResponse.class);

        gatewayContainer.setConfirmationKey(response.getData().getConfirmationKey());

        assertNull(response.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(response.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(response.getData().getConfirmationKey(),
                "В поле confirmationKey null");

        return response;
    }

    @Step("Step | Регистрация с зарегистрированным номером телефона")
    public void signUpRegisteredPhoneStep(String registeredPhone) {
        log.info("Step | Регистрация с зарегистрированным номером телефона");
        SignUpResponse response = authService.postSignUp(new SignUpRequest(
                        registeredPhone,
                        "captcha"))
                .statusCode(SC_CONFLICT)
                .contentType(ContentType.JSON)
                .extract().as(SignUpResponse.class);

        assertNull(response.getData(),
                "При ответе с ошибкой - поле data не пустое");
        assertEquals(ErrorMessage.NUMBER_EXISTS.getMessage(), response.getErrorMessage(),
                "При ответе с ошибкой вернулось неверное сообщение в поле errorMessage");
    }

    @Step("Step | Регистрация. Верификация СМС кода")
    public void signUpVerifyStep(String otp) {
        log.info("Step | Регистрация. Верификация СМС кода");
        authService.postSignUpVerify(new SignUpVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        otp))
                .statusCode(SC_OK);
    }

    @Step("Step | Регистрация с НЕверным СМС кодом")
    public void signUpVerifyInvalidOtpStep(String invalidOtp) {
        log.info("Step | Регистрация с НЕверным СМС кодом");
        SignUpVerifyResponse response = authService.postSignUpVerify(new SignUpVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        invalidOtp))
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignUpVerifyResponse.class);

        assertNull(response.getData(),
                "Верификация СМС с ошибкой вернуло НЕ пустое поле data");
        assertEquals(ErrorMessage.INCOMPATIBLE_OTP.getMessage(), response.getErrorMessage(),
                "Верификация СМС с ошибкой вернуло НЕверное errorMessage");
    }

    @Step("Step | Верификация с просроченным СМС кодом")
    public void signUpVerifyExpiredOtpStep() {
        log.info("Step | Верификация с просроченным СМС кодом");
        //todo избавиться от tread.sleep
        try {
            Thread.sleep(otpTimer);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка при ожидании истечения срока действия СМС кода", e);
        }
        SignUpVerifyResponse response = authService.postSignUpVerify(new SignUpVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        gatewayContainer.getUser().getOtp()
                ))
                .statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract().as(SignUpVerifyResponse.class);

        assertNull(response.getData(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула не пустое поле data");
        assertEquals(ErrorMessage.EXPIRED_OTP.getMessage(), response.getErrorMessage(),
                "Запрос Верификация OTP с протухшим СМС кодом вернула неверное errorMessage");
    }

    @Step("Step | Регистрация. Установка пароля")
    public SignUpSetPasswordResponse signUpSetPasswordStep(String password) {
        log.info("Step | Регистрация. Установка пароля");
        SignUpSetPasswordResponse response = authService.postSignUpSetPassword(new SignUpSetPasswordRequest(
                        gatewayContainer.getConfirmationKey(),
                        gatewayContainer.getUser().getPassword()))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignUpSetPasswordResponse.class);

        gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
        gatewayContainer.setUserRefreshToken(response.getData().getRefreshToken());

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
    public void signUpInvalidPasswordStep(Map<String, String> invalidPasswords) {
        log.info("Step | Регистрация с неверным паролем");
        for (var invalidPassword : invalidPasswords.entrySet()) {
            SignUpSetPasswordResponse response = authService.postSignUpSetPassword(new SignUpSetPasswordRequest(
                            gatewayContainer.getConfirmationKey(),
                            invalidPassword.getValue()))
                    .statusCode(SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .extract().as(SignUpSetPasswordResponse.class);

            assertNull(response.getData(),
                    "При ошибке в ответе поле data не пустое");
            assertEquals(invalidPassword.getKey(), response.getErrorMessage(),
                    "При неверном пароле в ответе вернулся неверный errorMessage");
        }
    }

    @Step("Step | Полный сброс паролял")
    public void resetPasswordE2eStep(String newPassword) {
        log.info("Step | Полный сброс паролял");
        resetPasswordStep();
        resetPasswordVerifyStep(gatewayContainer.getUser().getOtp());
        resetPasswordSetPasswordStep(newPassword);
    }

    @Step("Step | Сброс пароля зарегистрированного пользователя")
    public ResetPasswordResponse resetPasswordStep() {
        log.info("Step | Сброс пароля зарегистрированного пользователя");
        ResetPasswordResponse response = authService.postResetPassword(new ResetPasswordRequest(
                        gatewayContainer.getUser().getPhoneNumber(),
                        "captcha"))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResetPasswordResponse.class);

        gatewayContainer.setConfirmationKey(response.getData().getConfirmationKey());

        assertNull(response.getErrorMessage(),
                "При сбросе пароля вернулся errorMessage");
        assertNotNull(response.getData(),
                "При сбросе пароля вернулся пустой data");
        assertNotNull(response.getData().getConfirmationKey(),
                "При сбросе пароля вернулся пустой confirmationKey");

        return response;
    }

    @Step("Step | Сброс пароля НЕзарегистрированного пользователя")
    public ResetPasswordResponse resetPasswordInvalidPhoneStep(String invalidPhone) {
        log.info("Step | Сброс пароля НЕзарегистрированного пользователя");
        ResetPasswordResponse response = authService.postResetPassword(new ResetPasswordRequest(
                        invalidPhone,
                        "captcha"))
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
    public void resetPasswordVerifyStep(String otp) {
        log.info("Step | Сброс пароля. Верификация СМС кода");
        authService.postResetPasswordVerify(new ResetPasswordVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        gatewayContainer.getUser().getOtp()))
                .statusCode(SC_OK);
    }

    @Step("Step | Сброс пароля с неверный СМС кодом")
    public GatewayResponse resetPasswordVerifyInvalidOtpStep(String invalidOtp) {
        log.info("Step | Сброс пароля с неверный СМС кодом");
        GatewayResponse response = authService.postResetPasswordVerify(new ResetPasswordVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        invalidOtp
                ))
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
    public GatewayResponse resetPasswordVerifyInvalidLengthOtpStep(String invalidOtp) {
        log.info("Step | Сброс пароля. Неверная длина СМС кода");
        GatewayResponse response = authService.postResetPasswordVerify(new ResetPasswordVerifyRequest(
                        gatewayContainer.getConfirmationKey(),
                        invalidOtp))
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
    public void resetPasswordSetPasswordStep(String password) {
        log.info("Step | Сброс пароля. Установка нового пароля");
        authService.postResetPasswordSetPassword(new ResetPasswordSetPasswordRequest(
                        gatewayContainer.getConfirmationKey(),
                        password))
                .statusCode(SC_OK);
    }

    @Step("Step | Сброс пароля. Неверный пароль")
    public GatewayResponse resetPasswordInvalidPasswordStep(Map<String, String> invalidPasswords) {
        log.info("Step | Сброс пароля. Неверный пароль");
        GatewayResponse response = null;
        for (var invalidPassword : invalidPasswords.entrySet()) {
            response = authService.postResetPasswordSetPassword(new ResetPasswordSetPasswordRequest(
                            gatewayContainer.getConfirmationKey(),
                            invalidPassword.getValue()))
                    .statusCode(SC_BAD_REQUEST)
                    .contentType(ContentType.JSON)
                    .extract().as(GatewayResponse.class);

            assertNull(response.getData(),
                    "Сброс пароля. Неверный пароль. Поле data не null");
            assertEquals(invalidPassword.getKey(), response.getErrorMessage(),
                    "Сброс пароля. Неверный пароль. Вернулся неверный errorMessage");
        }
        if (response == null) {
            throw new RuntimeException("Сброс пароля. Неверный пароль. Пустой response");
        }
        return response;
    }

    @Step("Step | Sign out авторизованного пользователя")
    public void signOutStep() {
        log.info("Step | Sign out авторизованного пользователя");
        authService.postSignOut(new SignOutRequest(
                        gatewayContainer.getUser().getDeviceId()))
                .statusCode(SC_OK);
    }

    @Step("Step | Обновление access и refresh токена")
    public RefreshTokenResponse refreshTokenStep() {
        log.info("Step | Обновление access и refresh токена");
        RefreshTokenResponse response = authService.postRefreshToken(new RefreshTokenRequest(
                        gatewayContainer.getUserAccessToken(),
                        gatewayContainer.getUserRefreshToken(),
                        gatewayContainer.getUser().getDeviceId()))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(RefreshTokenResponse.class);

        gatewayContainer.setUserAccessToken(response.getData().getAccessToken());
        gatewayContainer.setUserRefreshToken(response.getData().getRefreshToken());

        assertNotNull(response.getData(),
                "Обновление токена. При положительном сценарии не вернулась data");
        assertNull(response.getErrorMessage(),
                "Обновление токена. При положительном сценарии вернулась ошибка");
        assertNotNull(response.getData().getAccessToken(),
                "Обновление токена. При положительном сценарии не вернулся access token");
        assertNotNull(response.getData().getRefreshToken(),
                "Обновление токена. При положительном сценарии не вернулся refresh token");
        assertNotNull(response.getData().getAccessTokenType(),
                "Обновление токена. При положительном сценарии не вернулся тип токена");

        return response;
    }

    @Step("Precondition | Регистрация. Удаление пользователя по номеру телефона")
    public void deleteUserByPhonePrecondition(String phoneNumber) {
        log.info("Precondition | Удаление пользователя по номеру телефона");
        signInAdminE2eStep();
        GetUsersResponse getUsersResponse = usersServiceStep.getUsersStep();
        usersServiceStep.deleteUserByPhone(phoneNumber, getUsersResponse);
    }

    @Step("Precondition | Сброс пароля")
    public void resetPasswordPrecondition(User user, String newPassword) {
        log.info("Precondition | Сброс пароля");
        resetPasswordE2eStep(newPassword);
        signInInvalidPasswordStep(user.getPassword());
    }

    @Step("Step | Полная авторизация администратора")
    public SignInVerifyResponse signInAdminE2eStep() {
        log.info("Step | Полная авторизация администратора");
        signInAdminStep();
        return signInVerifyAdminStep();
    }

    @Step("Step | Авторизация администратора")
    public SignInResponse signInAdminStep() {
        log.info("Step | Авторизация администратора");
        SignInResponse response = authService.postSignIn(
                        gatewayContainer.getAdmin().getPhoneNumber(),
                        gatewayContainer.getAdmin().getPassword(),
                        gatewayContainer.getAdmin().getDeviceId())
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInResponse.class);

        gatewayContainer.setAdminConfirmationKey(response.getData().getConfirmationKey());

        return response;
    }

    @Step("Step | Верификация администратора с верным СМС кодом")
    public SignInVerifyResponse signInVerifyAdminStep() {
        log.info("Step | Верификация администратора с верным СМС кодом");
        SignInVerifyResponse response = authService.postSignInVerify(new SignInVerifyRequest(
                        gatewayContainer.getAdmin().getDeviceId(),
                        gatewayContainer.getAdminConfirmationKey(),
                        gatewayContainer.getAdmin().getOtp()))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(SignInVerifyResponse.class);

        gatewayContainer.setAdminAccessToken(response.getData().getAccessToken());

        return response;
    }
}
