package tests.gateway.auth;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import tests.gateway.GatewayTest;
import uz.annotations.allure.Epic;
import uz.annotations.allure.Feature;
import uz.annotations.allure.Story;
import uz.gateway.services.asserts.AuthServiceAssert;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.testdata.pojo.User;

@Owner("Bulat Maskurov")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests extends GatewayTest {

    AuthServiceAssert authService = new AuthServiceAssert();

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

            ResponseSignIn responseSignIn = authService.postSignIn(
                            user.getPhoneNumber(),
                            user.getPassword(),
                            user.getDeviceId())
                    .statusCode(200)
                    .extract().as(ResponseSignIn.class);

            ResponseSignInVerify responseSignInVerify = authService.postSignInVerify(new RequestSignInVerify(
                            user.getDeviceId(),
                            responseSignIn.getData().getConfirmationKey(),
                            "999999"))
                    .statusCode(200)
                    .extract().as(ResponseSignInVerify.class);

            authService.postSignInAssertPositive(responseSignInVerify);
        }
    }

    @Nested
    @Owner("Bulat Maskurov")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Sign-in")
    public class SignUpTests {

        @Test
        @AllureId("1118")
        @Tags({@Tag("positive")})
        @DisplayName("Sign-up | Валидные данные")
        public void signUpTest() {


        }
    }
}