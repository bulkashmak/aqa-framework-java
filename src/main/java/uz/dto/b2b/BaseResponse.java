package uz.dto.b2b;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    @JsonProperty("data")
    List data;
    @JsonProperty("errorMessage")
    String errorMessage;
    @JsonProperty("timestamp")
    Long timestamp;
}
