package tests;

import org.springframework.boot.test.context.SpringBootTest;
import uz.ApiBuisnessApplication;
import uz.gateway.AuthService;
import uz.gateway.testdata.TestDataProvider;

@SpringBootTest(classes = ApiBuisnessApplication.class)
public abstract class BaseTest {

    protected TestDataProvider testDataProvider = new TestDataProvider();
    protected AuthService authService = new AuthService();


//    @BeforeAll
//    public static void setUp() {
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
//    }
}
