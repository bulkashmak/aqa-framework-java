package uz.tests.gateway.auth;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import uz.tests.gateway.GatewayTest;
import uz.annotations.allure.Epic;
import uz.annotations.allure.Feature;
import uz.annotations.allure.Story;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signUp.request.RequestSignUp;
import uz.gateway.dto.auth.signUp.request.RequestSignUpSetPassoword;
import uz.gateway.dto.auth.signUp.request.RequestSignUpVerify;
import uz.gateway.dto.auth.signUp.response.ResponseSignUp;
import uz.gateway.services.auth.AuthServiceStep;
import uz.gateway.services.auth.enums.SignUpPasswordError;
import uz.gateway.testdata.pojo.User;

import java.util.Map;

@Owner("Bulat Maskurov")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests extends GatewayTest {

    AuthServiceStep authService = new AuthServiceStep();

    @Nested
    @Owner("Bulat Maskurov")
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

            ResponseSignIn responseSignIn = authService.signInStep(user);

            authService.signInVerifyStep(new RequestSignInVerify(
                    user.getDeviceId(),
                    responseSignIn.getData().getConfirmationKey(),
                    user.getOtp()));
        }

        @Test
        @AllureId("2094")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный номер телефона")
        public void signInInvalidPhoneTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.deleteUserByPhone(user.getPhoneNumber());

            authService.signInInvalidPhoneStep(user);
        }

        @Test
        @AllureId("2095")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный СМС код")
        public void signInInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("default");

            ResponseSignIn responseSignIn = authService.signInStep(user);
            authService.signInVerifyInvalidOtpStep(new RequestSignInVerify(
                    user.getDeviceId(),
                    responseSignIn.getData().getConfirmationKey(),
                    "000000"));
        }

        @Test
        @AllureId("2096")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный пароль")
        public void signInInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("default");

            authService.signInInvalidPasswordStep(user, user.getPassword() + 1);
        }

        @Test
        @AllureId("2099")
        @Tag("negative")
        @DisplayName("Sign-in | Срок действия СМС кода истек")
        public void signInOtpExpiredTest() {

            User user = testDataProvider.getUserByAlias("default");

            ResponseSignIn responseSignIn = authService.signInStep(user);
            authService.signInVerifyOtpExpiredStep(new RequestSignInVerify(
                    user.getDeviceId(),
                    responseSignIn.getData().getConfirmationKey(),
                    user.getOtp()));
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
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
            testDataProvider.deleteUserByPhone(user.getPhoneNumber());

            ResponseSignUp responseSignUp = authService.signUpStep(new RequestSignUp(
                    user.getPhoneNumber(), "captcha"));

            authService.signUpVerifyStep(new RequestSignUpVerify(
                    responseSignUp.getData().getConfirmationKey(), user.getOtp()));

            authService.signUpSetPasswordStep(
                    200,
                    new RequestSignUpSetPassoword(
                            responseSignUp.getData().getConfirmationKey(),
                            user.getPassword()));
        }

        @Test
        @AllureId("2103")
        @Tag("negative")
        @DisplayName("Sign-up | Зарегистрированный номер телефона")
        public void signUpRegisteredPhoneTest() {

            User user = testDataProvider.getUserByAlias("user");

            authService.signUpRegisteredPhoneStep(new RequestSignUp(
                    user.getPhoneNumber(), "captcha"));
        }

        @Test
        @AllureId("2107")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный СМС код")
        public void signUpInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.deleteUserByPhone(user.getPhoneNumber());

            ResponseSignUp responseSignUp = authService.signUpStep(new RequestSignUp(
                    user.getPhoneNumber(), "captcha"));
            authService.signUpVerifyInvalidOtpStep(new RequestSignUpVerify(
                    responseSignUp.getData().getConfirmationKey(), "000000"));
        }

        @Test
        @AllureId("2108")
        @Tag("negative")
        @DisplayName("Sign-up | Срок действия СМС кода истек")
        public void signUpExpiredOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.deleteUserByPhone(user.getPhoneNumber());

            ResponseSignUp responseSignUp = authService.signUpStep(new RequestSignUp(
                    user.getPhoneNumber(), "captcha"));
            authService.signUpVerifyExpiredOtpStep(new RequestSignUpVerify(
                    responseSignUp.getData().getConfirmationKey(), user.getOtp()));
        }

        @Test
        @AllureId("2104")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный пароль")
        public void signUpInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.deleteUserByPhone(user.getPhoneNumber());

            Map<String, String> invalidPasswords = Map.of(
                    SignUpPasswordError.TOO_SHORT.getError(), "asdf123",
                    SignUpPasswordError.TOO_LONG.getError(), "a12345678901234567890",
                    SignUpPasswordError.INVALID_CHARACTERS.getError(), "фあasdf1234",
                    SignUpPasswordError.NO_LETTER.getError(), "12345678",
                    SignUpPasswordError.NO_DIGIT.getError(), "asdfghjk"
            );

            ResponseSignUp responseSignUp = authService.signUpStep(new RequestSignUp(
                    user.getPhoneNumber(), "captcha"));

            authService.signUpVerifyStep(new RequestSignUpVerify(
                    responseSignUp.getData().getConfirmationKey(), user.getOtp()));

            for (var entry : invalidPasswords.entrySet()) {
                authService.signUpInvalidPasswordStep(new RequestSignUpSetPassoword(
                        responseSignUp.getData().getConfirmationKey(),
                        entry.getValue()),
                        entry.getKey());
            }
        }
    }
}