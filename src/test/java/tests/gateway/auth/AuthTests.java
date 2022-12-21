package tests.gateway.auth;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import tests.gateway.GatewayTest;
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
import uz.gateway.testdata.pojo.User;

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
        @Tags({@Tag("positive")})
        @DisplayName("Sign-in | Валидные данные")
        public void signInTest() {

            User user = testDataProvider.getUserByAlias("default");

            ResponseSignIn responseSignIn = authService.signInStep(
                    200, user);

            authService.signInVerifyStep(200,
                    new RequestSignInVerify(
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

            ResponseSignUp responseSignUp = authService.signUpStep(200, new RequestSignUp(
                    user.getPhoneNumber(),
                    "captcha"));

            authService.signUpVerifyStep(200, new RequestSignUpVerify(
                    responseSignUp.getData().getConfirmationKey(),
                    user.getOtp()));

            authService.signUpSetPasswordStep(
                    200,
                    new RequestSignUpSetPassoword(
                            responseSignUp.getData().getConfirmationKey(),
                            user.getPassword()));
        }
    }
}