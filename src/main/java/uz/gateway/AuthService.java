package uz.gateway;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.dto.auth.signIn.RequestSignInVerify;

import static io.restassured.RestAssured.given;

@Service
@Slf4j
public class AuthService extends GatewayClient {

    @Step("AuthService: Авторизация")
    public ValidatableResponse postSignIn(String phoneNumber, String password, String deviceId) {
        log.info("AuthService: Авторизация");
        return given()
                .spec(defaultSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("phoneNumber", phoneNumber)
                .formParam("password", password)
                .formParam("deviceId", deviceId)
                .header("lang", "RU")
                .post("/auth/sign-in")
                .then();
    }

    @Step("AuthService: Верификация OTP")
    public ValidatableResponse postSignInVerify(RequestSignInVerify body) {
        log.info("AuthService: Верификация OTP");
        return given()
                .spec(defaultSpec)
                .body(body)
                .post("/auth/sign-in/verify")
                .then();
    }
}
