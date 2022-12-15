package uz.gateway.dto.auth.signIn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestSignInVerify {

    @JsonProperty("deviceId")
    String deviceId;
    @JsonProperty("confirmationKey")
    String confirmationKey;
    @JsonProperty("code")
    String code;
}
