package uz.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Контекст хранит в себе значения, необходимые во время прохождения теста
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayContext {

    private String accessToken;
    private String refreshToken;
    private String deviceId;
    private String confirmationKey;
    private String publicId;
    private String id;
}
