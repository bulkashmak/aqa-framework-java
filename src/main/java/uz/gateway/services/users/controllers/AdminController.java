package uz.gateway.services.users.controllers;

import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayClient;
import uz.gateway.GatewayContainer;

import static io.restassured.RestAssured.given;

@Slf4j
@Service
public class AdminController extends GatewayClient {

    @Autowired
    GatewayContainer gatewayContainer;

    public ValidatableResponse getUsers(String accessToken) {
        log.info("GET запрос {}", Path.GET_USERS.getPath());
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .get(Path.GET_USERS.getPath())
                .then();
    }

    public ValidatableResponse getUser(int userId) {
        log.info("GET запрос {}", Path.GET_USER.getPath());
        String path = String.format(Path.GET_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .get(path)
                .then();
    }

    public ValidatableResponse postLockUser(int userId) {
        log.info("POST запрос {}", Path.POST_LOCK_USER.getPath());
        String path = String.format(Path.POST_LOCK_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .post(path)
                .then();
    }

    public ValidatableResponse deleteUser(String accessToken, int userId) {
        log.info("DELETE запрос {}", Path.DELETE_USER.getPath());
        String path = String.format(Path.DELETE_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .delete(path)
                .then();
    }

    public enum Path {
        GET_USERS("/users/admin/users"),
        GET_USER("/users/admin/users/%s"),
        POST_LOCK_USER("/users/admin/users/%s/lock"),
        DELETE_USER("/users/admin/users/%s");

        private final String path;

        Path(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
