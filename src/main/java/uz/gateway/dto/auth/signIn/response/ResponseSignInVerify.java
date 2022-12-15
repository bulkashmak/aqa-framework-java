package uz.gateway.dto.auth.signIn.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSignInVerify {

    @JsonProperty("accessToken")
    String accessToken;
    @JsonProperty("accessToken")
    String refreshToken;
    @JsonProperty("refreshTokenExp")
            @JsonPropertyDescription("timestamp")
    Long refreshTokenExp;
    @JsonProperty("accessTokenType")
    String accessTokenType;
}
