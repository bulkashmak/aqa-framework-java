package tests.gateway;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;
import uz.annotations.Epic;
import uz.annotations.Feature;
import uz.annotations.Story;
import uz.gateway.AuthService;
import uz.dto.auth.signIn.ResponseSignIn;

@Owner("Bulat Maskurov")
@Epic("Gateway API")
@Feature("Auth service")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests {

    AuthService authService = new AuthService();

    @Test
    @AllureId("1117")
    @Story("Sign-in")
    @Tags({@Tag("positive")})
    @DisplayName("Sign-in | Валидные данные")
    public void SignInTest() {

        String phoneNumber = "998917771420";
        String password = "uzum2022";
        String deviceId = "test_device_id";

        ResponseSignIn responseSignIn = authService.postSignIn(
                        phoneNumber,
                        password,
                        deviceId)
                .statusCode(200)
                .extract().as(ResponseSignIn.class);

        System.out.println(responseSignIn.getData().getConfirmationKey());
    }
}
