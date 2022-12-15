package uz.gateway;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;
import uz.util.ApiProperties;

@Service
public class GatewayClient {

    public RequestSpecification defaultSpec;

    public GatewayClient() {
        this.defaultSpec = new RequestSpecBuilder()
                .setBaseUri(ApiProperties.getProperty("gatewayApiUri"))
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 5000)))
                .build();
    }
}