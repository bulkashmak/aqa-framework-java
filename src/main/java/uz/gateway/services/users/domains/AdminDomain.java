package uz.gateway.services.users.domains;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import uz.gateway.services.users.UsersService;

import static io.restassured.RestAssured.given;

@Slf4j
public class AdminDomain extends UsersService {

    public Response getUsers(String accessToken) {
        log.info("GET запрос {}", Path.GET_USERS.getPath());
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .get(Path.GET_USERS.getPath());
    }

    public Response getUser(int userId) {
        log.info("GET запрос {}", Path.GET_USER.getPath());
        String path = String.format(Path.GET_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .get(path);
    }

    public Response postLockUser(int userId) {
        log.info("POST запрос {}", Path.POST_LOCK_USER.getPath());
        String path = String.format(Path.POST_LOCK_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .post(path);
    }

    public Response deleteUser(String accessToken, int userId) {
        log.info("DELETE запрос {}", Path.DELETE_USER.getPath());
        String path = String.format(Path.DELETE_USER.getPath(), userId);
        return given()
                .spec(defaultSpec)
                .header("Authorization", String.format("Bearer %s", accessToken))
                .delete(path);
    }

    protected enum Path {
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
