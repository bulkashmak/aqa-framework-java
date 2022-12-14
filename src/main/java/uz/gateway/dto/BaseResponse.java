package uz.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/*
 * Дэфолтная модель ответа Gateway API
 */
@Data
public class BaseResponse {
    @JsonProperty("data")
    protected Object data;
    @JsonProperty("errorMessage")
    protected String errorMessage;
    @JsonProperty("timestamp")
    protected Long timestamp;
}
