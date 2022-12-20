package uz.gateway.services.auth;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.dto.auth.signUp.response.ResponseSignUpSetPassword;

@Slf4j
public class AuthServiceAssert extends AuthService {

    @Step("[ПРОВЕРКА] Авторизация")
    public void postAuthAssertPositive(ResponseSignInVerify responseSignInVerify) {
        log.info("[ПРОВЕРКА] Авторизация");
        Assert.assertNull(responseSignInVerify.getErrorMessage());
        Assert.assertNotNull(responseSignInVerify.getData().getAccessToken());
        Assert.assertNotNull(responseSignInVerify.getData().getRefreshToken());
        Assert.assertNotNull(responseSignInVerify.getData().getAccessTokenType());
    }

    @Step("[ПРОВЕРКА] Авторизация")
    public void postAuthAssertPositive(ResponseSignUpSetPassword responseSignUpSetPassword) {
        log.info("[ПРОВЕРКА] Авторизация");
        Assert.assertNull(responseSignUpSetPassword.getErrorMessage());
        Assert.assertNotNull(responseSignUpSetPassword.getData().getAccessToken());
        Assert.assertNotNull(responseSignUpSetPassword.getData().getRefreshToken());
        Assert.assertNotNull(responseSignUpSetPassword.getData().getAccessTokenType());
    }
}
