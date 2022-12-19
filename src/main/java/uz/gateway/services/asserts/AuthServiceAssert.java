package uz.gateway.services.asserts;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.services.AuthService;

@Slf4j
public class AuthServiceAssert extends AuthService {

    @Step("[ПРОВЕРКА] Авторизация")
    public void postSignInAssertPositive(ResponseSignInVerify responseSignInVerify) {
        log.info("[ПРОВЕРКА] Авторизация");
        Assert.assertNull(responseSignInVerify.getErrorMessage());
        Assert.assertNotNull(responseSignInVerify.getData().getAccessToken());
        Assert.assertNotNull(responseSignInVerify.getData().getRefreshToken());
        Assert.assertNotNull(responseSignInVerify.getData().getAccessTokenType());
    }
}
