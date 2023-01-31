package uz.subscriber

import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.HttpClientConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import uz.gateway.testdata.TestDataProvider

abstract class SubscriberClient {

    lateinit var defaultSpec: RequestSpecification

    fun SubscriberClient() {
        defaultSpec = RequestSpecBuilder()
                .setBaseUri(TestDataProvider().getClientURI("", ""))
                .addFilter(ResponseLoggingFilter())
                .addFilter(RequestLoggingFilter())
                .setConfig(RestAssuredConfig.config()
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", 5000)))
                .build()
    }
}