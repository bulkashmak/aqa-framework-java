package uz.gateway.services.users.domains;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import uz.gateway.services.users.UsersService;

import static io.restassured.RestAssured.given;

@Slf4j
public class AdminOperation extends UsersService {

    @Step("[ШАГ] Получение списка пользователей")
    public ValidatableResponse getUsers(String accessToken) {
        log.info("[ШАГ] Получение списка пользователей");
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .get(Path.GET_USERS.getPath())
                .then();
    }

    @Step("[ШАГ] Получить пользователя по id")
    public ValidatableResponse getUser(int userId) {
        log.info("[ШАГ] Получить пользователя по id");

        String path = String.format(Path.GET_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .get(path)
                .then();
    }

    @Step("[ШАГ] Заблокировать пользователя по id")
    public ValidatableResponse postLockUser(int userId) {
        log.info("[ШАГ] Заблокировать пользователя по id");

        String path = String.format(Path.POST_LOCK_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .post(path)
                .then();
    }

    @Step("[ШАГ] Удалить пользователя по id")
    public ValidatableResponse deleteUser(String accessToken, int userId) {
        log.info("[ШАГ] Удалить пользователя по id");

        String path = String.format(Path.DELETE_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .delete(path)
                .then();
    }

    private enum Path {
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
