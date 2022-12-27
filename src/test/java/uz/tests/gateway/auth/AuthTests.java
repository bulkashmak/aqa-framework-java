package uz.tests.gateway.auth;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import uz.annotations.allure.Epic;
import uz.annotations.allure.Feature;
import uz.annotations.allure.Story;
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

            authServiceStep.signInStep();
            authServiceStep.signInVerifyStep(user.getOtp());
        }

        @Test
        @AllureId("2094")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный номер телефона")
        public void signInInvalidPhoneTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            authServiceStep.signInInvalidPhoneStep(user.getPhoneNumber());
        }

        @Test
        @AllureId("2095")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный СМС код")
        public void signInInvalidOtpTest() {

            testDataProvider.getUserByAlias("default");

            authServiceStep.signInStep();
            authServiceStep.signInVerifyInvalidOtpStep("000000");
        }

        @Test
        @AllureId("2096")
        @Tag("negative")
        @DisplayName("Sign-in | Неверный пароль")
        public void signInInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("default");

            authServiceStep.signInInvalidPasswordStep(user.getPassword() + 1);
        }

        @Test
        @AllureId("2099")
        @Tag("negative")
        @DisplayName("Sign-in | Срок действия СМС кода истек")
        public void signInOtpExpiredTest() {

            testDataProvider.getUserByAlias("default");

            authServiceStep.signInStep();
            authServiceStep.signInVerifyOtpExpiredStep();
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
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            authServiceStep.signUpStep();
            authServiceStep.signUpVerifyStep(user.getOtp());
            authServiceStep.signUpSetPasswordStep(user.getPassword());

            authServiceCheck.userCreatedCheck();
        }

        @Test
        @AllureId("2103")
        @Tag("negative")
        @DisplayName("Sign-up | Зарегистрированный номер телефона")
        public void signUpRegisteredPhoneTest() {

            User user = testDataProvider.getUserByAlias("user");

            authServiceStep.signUpRegisteredPhoneStep(user.getPhoneNumber());
        }

        @Test
        @AllureId("2107")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный СМС код")
        public void signUpInvalidOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            authServiceStep.signUpStep();
            authServiceStep.signUpVerifyInvalidOtpStep("000000");
        }

        @Test
        @AllureId("2108")
        @Tag("negative")
        @DisplayName("Sign-up | Срок действия СМС кода истек")
        public void signUpExpiredOtpTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            authServiceStep.signUpStep();
            authServiceStep.signUpVerifyExpiredOtpStep();
        }

        @Test
        @AllureId("2104")
        @Tag("negative")
        @DisplayName("Sign-up | Неверный пароль")
        public void signUpInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            Map<String, String> invalidPasswords = Map.of(
                    SignUpPasswordError.TOO_SHORT.getError(), "asdf123",
                    SignUpPasswordError.TOO_LONG.getError(), "a12345678901234567890",
                    SignUpPasswordError.INVALID_CHARACTERS.getError(), "фあasdf1234",
                    SignUpPasswordError.NO_LETTER.getError(), "12345678",
                    SignUpPasswordError.NO_DIGIT.getError(), "asdfghjk"
            );

            authServiceStep.signUpStep();
            authServiceStep.signUpVerifyStep(user.getOtp());
            authServiceStep.signUpInvalidPasswordStep(invalidPasswords);
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

            authServiceStep.resetPasswordStep();
            authServiceStep.resetPasswordVerifyStep(user.getOtp());
            authServiceStep.resetPasswordSetPasswordStep(user.getPassword());

            authServiceCheck.resetPasswordCheck();
        }

        @Test
        @AllureId("2724")
        @Tag("negative")
        @DisplayName("Reset password | Незарегистрированный номер телефона")
        public void resetPasswordInvalidPhoneTest() {

            User user = testDataProvider.getUserByAlias("delete");
            testDataProvider.getUserByAlias("admin");
            authServiceStep.deleteUserByPhonePrecondition(user.getPhoneNumber());

            authServiceStep.resetPasswordInvalidPhoneStep(user.getPhoneNumber());
        }

        @Test
        @AllureId("3537")
        @Tag("negative")
        @DisplayName("Reset password | Неверный СМС код")
        public void resetPasswordInvalidOtpTest() {

            testDataProvider.getUserByAlias("reset");

            authServiceStep.resetPasswordStep();
            authServiceStep.resetPasswordVerifyInvalidOtpStep("999991");
        }

        @Test
        @AllureId("4829")
        @Tag("negative")
        @DisplayName("Reset password | Неверная длина СМС кода")
        public void resetPasswordInvalidLengthOtpTest() {

            testDataProvider.getUserByAlias("reset");

            authServiceStep.resetPasswordStep();
            authServiceStep.resetPasswordVerifyInvalidLengthOtpStep("99999");
        }

        @Test
        @AllureId("4830")
        @Tag("negative")
        @DisplayName("Reset password | Неверный пароль")
        public void resetPasswordInvalidPasswordTest() {

            User user = testDataProvider.getUserByAlias("reset");

            Map<String, String> invalidPasswords = Map.of(
                    SignUpPasswordError.TOO_SHORT.getError(), "asdf123",
                    SignUpPasswordError.TOO_LONG.getError(), "a12345678901234567890",
                    SignUpPasswordError.INVALID_CHARACTERS.getError(), "фあasdf1234",
                    SignUpPasswordError.NO_LETTER.getError(), "12345678",
                    SignUpPasswordError.NO_DIGIT.getError(), "asdfghjk"
            );

            authServiceStep.resetPasswordStep();
            authServiceStep.resetPasswordVerifyStep(user.getOtp());
            authServiceStep.resetPasswordInvalidPasswordStep(invalidPasswords);
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
    @DisplayName("Sign-out tests")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Sign-out")
    public class SignOutTests {

        @Test
        @AllureId("5072")
        @Tag("positive")
        @DisplayName("Sign-out | Валидные данные")
        public void signOutTest() {

            testDataProvider.getUserByAlias("default");

            authServiceStep.signInE2eStep();
            authServiceStep.signOutStep();
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
    @DisplayName("Refresh token tests")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Refresh token")
    public class RefreshTokenTests {

        @Test
        @AllureId("5074")
        @Tag("positive")
        @DisplayName("Refresh token | Валидные данные")
        public void refreshTokenTest() {

            testDataProvider.getUserByAlias("default");

            authServiceStep.signInE2eStep();
            authServiceStep.refreshTokenStep();
        }
    }
}