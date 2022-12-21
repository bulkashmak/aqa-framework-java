package uz.gateway.services.auth;

import lombok.extern.slf4j.Slf4j;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.dto.auth.signUp.response.ResponseSignUp;
import uz.gateway.dto.auth.signUp.response.ResponseSignUpSetPassword;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AuthServiceCheck {

    public void signInStepCheck(ResponseSignIn responseSignIn) {
        log.info("signInStep валидация ответа");
        assertNull(responseSignIn.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(responseSignIn.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(responseSignIn.getData().getConfirmationKey(),
                "В поле confirmationKey null");
    }

    public void signInVerifyCheck(ResponseSignInVerify responseSignInVerify) {
        log.info("signInVerifyStep валидация ответа");
        assertNull(responseSignInVerify.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(responseSignInVerify.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(responseSignInVerify.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(responseSignInVerify.getData().getAccessTokenType(),
                "В поле accessTokenType null");
    }

    public void signUpCheck(ResponseSignUp responseSignUp) {
        log.info("signUpStep валидация ответа");
        assertNull(responseSignUp.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertTrue(responseSignUp.getData().getTtl() > 0,
                "В поле ttl срок действия otp <= 0");
        assertNotNull(responseSignUp.getData().getConfirmationKey(),
                "В поле confirmationKey null");
    }

    public void signUpSetPasswordCheck(ResponseSignUpSetPassword responseSignUpSetPassword) {
        log.info("signUpSetPasswordStep валидация ответа");
        assertNull(responseSignUpSetPassword.getErrorMessage(),
                "В поле errorMessage вернулась ошибка");
        assertNotNull(responseSignUpSetPassword.getData().getAccessToken(),
                "В поле accessToken null");
        assertNotNull(responseSignUpSetPassword.getData().getRefreshToken(),
                "В поле refreshToken null");
        assertNotNull(responseSignUpSetPassword.getData().getAccessTokenType(),
                "В поле accessTokenType null");
    }
}
