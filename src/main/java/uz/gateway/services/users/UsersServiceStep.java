package uz.gateway.services.users;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;
import uz.gateway.services.users.controllers.AdminController;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;

@Slf4j
@Service
public class UsersServiceStep {

    @Autowired
    AdminController adminController;

    @Step("[ШАГ] Получение списка пользователей")
    public ResponseGetUsers getUsersStep(String accessToken) {
        log.info("[ШАГ] Получение списка пользователей");
        return adminController.getUsers(accessToken)
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseGetUsers.class);
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
    public void deleteUserStep(String accessToken, int userId) {
        log.info("[ШАГ] Удалить пользователя по id");
        adminController.deleteUser(accessToken, userId)
                .statusCode(SC_OK);
    }
}
