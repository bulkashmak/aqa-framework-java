package tests.b2b;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uz.annotations.Epic;
import uz.annotations.Feature;
import uz.annotations.Story;
import uz.clients.b2b.AuthService;
import uz.dto.b2b.auth.signIn.ResponseSignIn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GatewayApiAuthTests extends BaseTest {

    AuthService authService = new AuthService();

    @Test
    @AllureId("1117")
    @Epic("Gateway API")
    @Feature("Auth service")
    @Story("Sign-in")
    @DisplayName("Авторизация | Валидные данные")
    public void SignInTest() {

        String phoneNumber = "998917771420";
        String password = "Uzum2022.";
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
