package uz.tests.gateway.auth;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import uz.annotations.allure.Epic;
import uz.annotations.allure.Feature;
import uz.annotations.allure.Story;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordSetPasswordRequest;
import uz.gateway.dto.auth.resetPassword.request.ResetPasswordVerifyRequest;
import uz.gateway.dto.auth.resetPassword.response.ResetPasswordResponse;
import uz.gateway.dto.auth.signIn.request.SignInVerifyRequest;
import uz.gateway.dto.auth.signIn.response.SignInResponse;
import uz.gateway.dto.auth.signUp.request.SignUpRequest;
import uz.gateway.dto.auth.signUp.request.SignUpSetPasswordRequest;
import uz.gateway.dto.auth.signUp.request.SignUpVerifyRequest;
import uz.gateway.dto.auth.signUp.response.SignUpResponse;
import uz.gateway.services.auth.AuthServiceCheck;
import uz.gateway.services.auth.AuthServiceStep;
import uz.gateway.services.auth.enums.SignUpPasswordError;
import uz.gateway.testdata.pojo.User;
import uz.tests.gateway.GatewayTest;

import java.util.Map;

@Owner("Bulat Maskurov")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests extends GatewayTest {

    @Autowired
    AuthServiceStep authServiceStep;
    @Autowired
    AuthServiceCheck authServiceCheck;

    @Nested
    @Owner("Bulat Maskurov")
    @DisplayName("Sign-in tests")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Sign-in")
    public class SignInTests {

        @Test
        @AllureId("1117")
        @Tag("positive")
        @DisplayName("Sign-in | Валидные данные")
        public void signInTest() {

            User user = testDataProvider.getUserByAlias("default");

            SignInResponse signInResponse = authServiceStep.signInStep(user);
            authServiceStep.signInVerifyStep(new SignInVerifyRequest(
                    user.getDeviceId(),
                    signInResponse.getData().getConfirmationKey(),
                    user.getOtp()));
        }

        @Test
        @AllureId("2094")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный номер телефона")
        public void signInInvalidPhoneTest() {

            User user = testDataProvider.getUserByAlias("delete");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            authServiceStep.signInInvalidPhoneStep(user);
        }

        @Test
        @AllureId("2095")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный СМС код")
        public void signInInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("default");

            SignInResponse signInResponse = authServiceStep.signInStep(user);
            authServiceStep.signInVerifyInvalidOtpStep(new SignInVerifyRequest(
                    user.getDeviceId(),
                    signInResponse.getData().getConfirmationKey(),
                    "000000"));
        }

        @Test
        @AllureId("2096")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный пароль")
        public void signInInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("default");

            authServiceStep.signInInvalidPasswordStep(user, user.getPassword() + 1);
        }

        @Test
        @AllureId("2099")
        @Tag("negative")
        @DisplayName("Sign-in | Срок действия СМС кода истек")
        public void signInOtpExpiredTest() {

            User user = testDataProvider.getUserByAlias("default");

            SignInResponse signInResponse = authServiceStep.signInStep(user);
            authServiceStep.signInVerifyOtpExpiredStep(new SignInVerifyRequest(
                    user.getDeviceId(),
                    signInResponse.getData().getConfirmationKey(),
                    user.getOtp()));
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
    @DisplayName("Sign-up tests")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Sign-up")
    public class SignUpTests {

        @Test
        @AllureId("1118")
        @Tags({@Tag("positive")})
        @DisplayName("Sign-up | Валидные данные")
        public void signUpTest() {

            User user = testDataProvider.getUserByAlias("user");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            SignUpResponse signUpResponse = authServiceStep.signUpStep(new SignUpRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.signUpVerifyStep(new SignUpVerifyRequest(
                    signUpResponse.getData().getConfirmationKey(), user.getOtp()));
            authServiceStep.signUpSetPasswordStep(
                    new SignUpSetPasswordRequest(
                            signUpResponse.getData().getConfirmationKey(),
                            user.getPassword()));

            authServiceCheck.userCreatedCheck(user, admin);
        }

        @Test
        @AllureId("2103")
        @Tag("negative")
        @DisplayName("Sign-up | Зарегистрированный номер телефона")
        public void signUpRegisteredPhoneTest() {

            User user = testDataProvider.getUserByAlias("user");

            authServiceStep.signUpRegisteredPhoneStep(new SignUpRequest(
                    user.getPhoneNumber(), "captcha"));
        }

        @Test
        @AllureId("2107")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный СМС код")
        public void signUpInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            SignUpResponse signUpResponse = authServiceStep.signUpStep(new SignUpRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.signUpVerifyInvalidOtpStep(new SignUpVerifyRequest(
                    signUpResponse.getData().getConfirmationKey(), "000000"));
        }

        @Test
        @AllureId("2108")
        @Tag("negative")
        @DisplayName("Sign-up | Срок действия СМС кода истек")
        public void signUpExpiredOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            SignUpResponse signUpResponse = authServiceStep.signUpStep(new SignUpRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.signUpVerifyExpiredOtpStep(new SignUpVerifyRequest(
                    signUpResponse.getData().getConfirmationKey(), user.getOtp()));
        }

        @Test
        @AllureId("2104")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный пароль")
        public void signUpInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("delete");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            Map<String, String> invalidPasswords = Map.of(
                    SignUpPasswordError.TOO_SHORT.getError(), "asdf123",
                    SignUpPasswordError.TOO_LONG.getError(), "a12345678901234567890",
                    SignUpPasswordError.INVALID_CHARACTERS.getError(), "фあasdf1234",
                    SignUpPasswordError.NO_LETTER.getError(), "12345678",
                    SignUpPasswordError.NO_DIGIT.getError(), "asdfghjk"
            );

            SignUpResponse signUpResponse = authServiceStep.signUpStep(new SignUpRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.signUpVerifyStep(new SignUpVerifyRequest(
                    signUpResponse.getData().getConfirmationKey(), user.getOtp()));
            for (var entry : invalidPasswords.entrySet()) {
                authServiceStep.signUpInvalidPasswordStep(new SignUpSetPasswordRequest(
                                signUpResponse.getData().getConfirmationKey(),
                                entry.getValue()),
                        entry.getKey());
            }
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
    @DisplayName("Reset password tests")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Reset password")
    public class ResetPasswordTests {

        @Test
        @AllureId("2362")
        @Tag("positive")
        @DisplayName("Reset password | Валидные данные")
        public void resetPasswordTest() {

            User user = testDataProvider.getUserByAlias("reset");
            authServiceStep.resetPasswordPrecondition(user, user.getPassword() + 1);

            ResetPasswordResponse resetPasswordResponse = authServiceStep.resetPasswordStep(new ResetPasswordRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.resetPasswordVerifyStep(new ResetPasswordVerifyRequest(
                    resetPasswordResponse.getData().getConfirmationKey(), user.getOtp()));
            authServiceStep.resetPasswordSetPasswordStep(new ResetPasswordSetPasswordRequest(
                    resetPasswordResponse.getData().getConfirmationKey(), user.getPassword()));

            authServiceStep.signInE2eStep(user);
        }

        @Test
        @AllureId("2724")
        @Tag("negative")
        @DisplayName("Reset password | Незарегистрированный номер телефона")
        public void resetPasswordInvalidPhoneTest() {

            User user = testDataProvider.getUserByAlias("delete");
            User admin = testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber(), admin);

            authServiceStep.resetPasswordInvalidPhoneStep(new ResetPasswordRequest(
                    user.getPhoneNumber(), "captcha"));
        }

        @Test
        @AllureId("3537")
        @Tag("negative")
        @DisplayName("Reset password | Неверный СМС код")
        public void resetPasswordInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("reset");

            ResetPasswordResponse resetPasswordResponse = authServiceStep.resetPasswordStep(new ResetPasswordRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.resetPasswordVerifyInvalidOtpStep(new ResetPasswordVerifyRequest(
                    resetPasswordResponse.getData().getConfirmationKey(), "999991"));
        }

        @Test
        @AllureId("4829")
        @Tag("negative")
        @DisplayName("Reset password | Неверный СМС код")
        public void resetPasswordInvalidLengthOtpTest() {

            User user = testDataProvider.getUserByAlias("reset");

            ResetPasswordResponse resetPasswordResponse = authServiceStep.resetPasswordStep(new ResetPasswordRequest(
                    user.getPhoneNumber(), "captcha"));
            authServiceStep.resetPasswordVerifyInvalidLengthOtpStep(new ResetPasswordVerifyRequest(
                    resetPasswordResponse.getData().getConfirmationKey(), "99999"));
        }
    }
}