package uz.gateway.dto.auth.signOut;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignOutRequest {

    @JsonProperty("deviceId")
    private String deviceId;
}
