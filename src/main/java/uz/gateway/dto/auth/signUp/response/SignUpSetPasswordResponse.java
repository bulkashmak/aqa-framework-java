package uz.gateway.dto.auth.signUp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.gateway.dto.auth.signIn.response.SignInVerifyResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpSetPasswordResponse {

    @JsonProperty("data")
    private SignInVerifyResponse.Data data;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("timestamp")
    private long timestamp;

    @lombok.Data
    public static class Data {
        @JsonProperty("accessToken")
        String accessToken;
        @JsonProperty("refreshToken")
        String refreshToken;
        @JsonProperty("refreshTokenExp")
        Long refreshTokenExp;
        @JsonProperty("accessTokenType")
        String accessTokenType;
    }
}
