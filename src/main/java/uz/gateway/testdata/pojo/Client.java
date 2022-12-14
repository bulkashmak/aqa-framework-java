package uz.gateway.testdata.pojo;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "alias",
        "port"
})
@Generated("jsonschema2pojo")
public class Client {

    @JsonProperty("alias")
    public String alias;
    @JsonProperty("port")
    public String port;
}