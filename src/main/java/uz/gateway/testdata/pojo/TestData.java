package uz.gateway.testdata.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "servers",
        "clients",
        "users"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestData {

    @JsonProperty("servers")
    private List<Server> servers;
    @JsonProperty("clients")
    private List<Client> clients;
    @JsonProperty("users")
    private List<User> users;
}
