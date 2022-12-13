package tests;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import uz.ApiBuisnessApplication;

@SpringBootTest(classes = ApiBuisnessApplication.class)
public abstract class BaseTest {
    //todo Static?
    public static String ADMIN_SESSION_TOKEN;

    @BeforeAll
    public static void authBackOfficeAdmin() {
//        //todo Hardcode?
//        AuthService authService = new AuthService();
//        String adminLogin = "testqa@uzum.com";
//        String adminPassword = "testqa";
//        String otpSecretAdmin = "c5phrygys5wvazxa72eu36c5kgmpjhmv";
//
//        RequestPostLogin requestPostLogin = new RequestPostLogin(adminLogin, adminPassword);
//
//        String cookie = authService.postBackOfficeLoginNew(requestPostLogin)
//                .statusCode(200)
//                .extract().cookie("_bo_api_session");
//
//        String otp = generateOtp(otpSecretAdmin);
//        RequestPostOtpAuth requestPostOtpAuth = new RequestPostOtpAuth(otp);
//        ADMIN_SESSION_TOKEN = authService.postBackOfficeOtpAuthNew(requestPostOtpAuth, cookie)
//                .statusCode(200)
//                .extract().cookie("_bo_api_session");
    }
}
