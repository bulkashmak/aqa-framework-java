package uz.gateway.dto.auth.signUp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignUpVerify {

    @JsonProperty("confirmationKey")
    private String confirmationKey;
    @JsonProperty("code")
    private String code;
}
