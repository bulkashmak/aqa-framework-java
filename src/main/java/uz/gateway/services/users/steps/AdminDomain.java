package uz.gateway.services.users.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;

@Slf4j
public class AdminDomain extends uz.gateway.services.users.domains.AdminDomain {

    @Step("[ШАГ] Получение списка пользователей")
    public ResponseGetUsers getUsersStep(int expectedStatusCode, String accessToken) {
        log.info("[ШАГ] Получение списка пользователей");
        Response response = getUsers(accessToken);

        Assert.assertEquals(String.format("GET запрос %s вернул неверный status code", Path.GET_USERS.getPath()),
                expectedStatusCode, response.getStatusCode());

        return response.getBody().as(ResponseGetUsers.class);
    }

    @Step("[ШАГ] Получить пользователя по id")
    public void getUserStep(int expectedStatusCode, int userId) {
        log.info("[ШАГ] Получить пользователя по id");
    }

    @Step("[ШАГ] Заблокировать пользователя по id")
    public void lockUserStep(int expectedStatusCode, int userId) {
        log.info("[ШАГ] Заблокировать пользователя по id");
    }

    @Step("[ШАГ] Удалить пользователя по id")
    public void deleteUserStep(int expectedStatusCode, String accessToken, int userId) {
        log.info("[ШАГ] Удалить пользователя по id");
        Response response = deleteUser(accessToken, userId);

        Assert.assertEquals(String.format("GET запрос %s вернул неверный status code", Path.GET_USER.getPath()),
                expectedStatusCode, response.getStatusCode());
    }
}
