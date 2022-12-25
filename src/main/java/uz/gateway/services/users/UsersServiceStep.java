package uz.gateway.services.users;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;
import uz.gateway.services.users.controllers.AdminController;

@Slf4j
@Service
public class UsersServiceStep {

    @Autowired
    AdminController adminController;

    @Step("[ШАГ] Получение списка пользователей")
    public ResponseGetUsers getUsersStep(int expectedStatusCode, String accessToken) {
        log.info("[ШАГ] Получение списка пользователей");
        Response response = adminController.getUsers(accessToken);

        Assert.assertEquals(
                String.format("GET запрос %s вернул неверный status code", AdminController.Path.GET_USERS.getPath()),
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
        Response response = adminController.deleteUser(accessToken, userId);

        Assert.assertEquals(
                String.format("GET запрос %s вернул неверный status code", AdminController.Path.GET_USER.getPath()),
                expectedStatusCode, response.getStatusCode());
    }
}
