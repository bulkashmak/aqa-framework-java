package uz.dto.auth.signIn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSignIn {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("timestamp")
    private long timestamp;
    @lombok.Data
    public static class Data {
        @JsonProperty("ttl")
        @JsonPropertyDescription("OTP timer timestamp")
        private long ttl;
        @JsonProperty("confirmationKey")
        private String confirmationKey;
    }
}
