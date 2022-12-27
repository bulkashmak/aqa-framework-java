package uz.gateway.dto.auth.refreshToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {

    @JsonProperty("data")
    private Data data;
    @JsonProperty("errorMessage")
    private Object errorMessage;
    @JsonProperty("timestamp")
    private Long timestamp;

    @lombok.Data
    public static class Data {

        @JsonProperty("accessToken")
        private String accessToken;
        @JsonProperty("refreshToken")
        private String refreshToken;
        @JsonProperty("refreshTokenExp")
        private Long refreshTokenExp;
        @JsonProperty("accessTokenType")
        private String accessTokenType;
    }
}
