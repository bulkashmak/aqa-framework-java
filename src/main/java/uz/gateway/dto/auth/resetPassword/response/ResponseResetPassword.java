package uz.gateway.dto.auth.resetPassword.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResetPassword {

    @JsonProperty("data")
    public Data data;
    @JsonProperty("errorMessage")
    public Object errorMessage;
    @JsonProperty("timestamp")
    public Long timestamp;

    @lombok.Data
    public static class Data {

        @JsonProperty("ttl")
        public Integer ttl;
        @JsonProperty("confirmationKey")
        public String confirmationKey;

    }
}
