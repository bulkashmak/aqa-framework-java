package uz.gateway;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class GatewayContainer {

    String userAccessToken;
    String userRefreshToken;

    String adminAccessToken;
    String adminRefreshToken;
}
