package uz.gateway;

import lombok.Data;
import org.springframework.stereotype.Component;
import uz.gateway.testdata.pojo.User;

@Data
@Component
public class GatewayContainer {

    String userAccessToken;
    String userRefreshToken;

    String adminAccessToken;
    String adminRefreshToken;

    String confirmationKey;
    String adminConfirmationKey;

    User user;
    User admin;
}
