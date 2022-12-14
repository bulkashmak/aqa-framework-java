package tests.gateway;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import tests.BaseTest;
import uz.annotations.allure.Epic;
import uz.annotations.allure.Feature;
import uz.annotations.allure.Story;
import uz.gateway.AuthService;
import uz.gateway.dto.auth.signIn.ResponseSignIn;
import uz.gateway.testdata.pojo.User;

@Owner("Bulat Maskurov")
@Epic("Gateway API")
@Feature("Auth service")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests extends BaseTest {
    AuthService authService = new AuthService();

    @Test
    @AllureId("1117")
    @Story("Sign-in")
    @Tags({@Tag("positive")})
    @DisplayName("Sign-in | Валидные данные")
    public void SignInTest() {

        User user = testDataGenerator.getUserByAlias("bulat");

        ResponseSignIn responseSignIn = authService.postSignIn(
                        user.getPhoneNumber(),
                        user.getPassword(),
                        user.getDeviceId())
                .statusCode(200)
                .extract().as(ResponseSignIn.class);

        System.out.println(responseSignIn.getData().getConfirmationKey());
    }
}
