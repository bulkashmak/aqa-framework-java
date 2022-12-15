package uz.gateway.testdata.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "alias",
        "port"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @JsonProperty("alias")
    public String alias;
    @JsonProperty("port")
    public String port;
}