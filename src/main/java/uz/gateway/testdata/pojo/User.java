package uz.gateway.testdata.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * POJO класс для пользователя в Gateway API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "alias",
        "phoneNumber",
        "password",
        "deviceId",
        "otp"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("password")
    private String password;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("otp")
    private String otp;
}
