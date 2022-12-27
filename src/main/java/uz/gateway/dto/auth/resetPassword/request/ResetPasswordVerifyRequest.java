package uz.gateway.dto.auth.resetPassword.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordVerifyRequest {

    @JsonProperty("confirmationKey")
    public String confirmationKey;
    @JsonProperty("code")
    public String code;
}
