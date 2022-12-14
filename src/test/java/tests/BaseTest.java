package tests;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import uz.ApiBuisnessApplication;
import uz.gateway.AuthService;
import uz.gateway.testdata.TestDataGenerator;
import uz.gateway.testdata.pojo.User;

@SpringBootTest(classes = ApiBuisnessApplication.class)
public abstract class BaseTest {

    protected TestDataGenerator testDataGenerator = new TestDataGenerator();

    @BeforeAll
    public static void setUp() {
        AuthService authService = new AuthService();
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
