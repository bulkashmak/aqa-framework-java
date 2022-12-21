package uz.gateway.dto.auth.signUp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignUp {

    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("captcha")
    private String captcha;
}
