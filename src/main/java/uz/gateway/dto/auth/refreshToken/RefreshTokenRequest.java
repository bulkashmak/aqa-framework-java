package uz.gateway.dto.auth.refreshToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    @JsonProperty("accessToken")
    public String accessToken;
    @JsonProperty("refreshToken")
    public String refreshToken;
    @JsonProperty("deviceId")
    public String deviceId;
}
