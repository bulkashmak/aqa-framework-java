package uz.gateway.dto.auth.signIn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInVerifyRequest {

    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("confirmationKey")
    private String confirmationKey;
    @JsonProperty("code")
    private String code;
}
