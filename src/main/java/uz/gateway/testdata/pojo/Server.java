package uz.gateway.testdata.pojo;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "alias",
        "uri"
})
@Generated("jsonschema2pojo")
public class Server {

    @JsonProperty("alias")
    public String alias;
    @JsonProperty("uri")
    public String uri;

}