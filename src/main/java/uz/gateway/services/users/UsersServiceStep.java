package uz.gateway.services.users;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayContainer;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;
import uz.gateway.services.auth.AuthServiceStep;
import uz.gateway.services.users.controllers.AdminController;
import uz.gateway.testdata.pojo.User;

import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;

@Slf4j
@Service
public class UsersServiceStep {

    @Autowired
    GatewayContainer gatewayContainer;

    @Autowired
    AdminController adminController;

    @Autowired
    AuthServiceStep authServiceStep;

    @Step("Step | Получение списка пользователей")
    public ResponseGetUsers getUsersStep() {
        log.info("Step | Получение списка пользователей");
        return adminController.getUsers()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseGetUsers.class);
    }

    @Step("Step | Получить пользователя по id")
    public void getUserStep(int userId) {
        log.info("Step | Получить пользователя по id");
        adminController.getUsers()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON);
    }

    @Step("Step | Заблокировать пользователя по id")
    public void lockUserStep(int userId) {
        log.info("Step | Заблокировать пользователя по id");
    }

    @Step("Step | Удалить пользователя по id")
    public void deleteUserStep(int userId) {
        log.info("Step | Удалить пользователя по id");
        adminController.deleteUser(userId)
                .statusCode(SC_OK);
    }

    @Step("Precondition | Удаление пользователя по номеру телефона")
    public void deleteUserByPhonePrecondition(String phoneNumber, User admin) {
        log.info("Precondition | Удаление пользователя по номеру телефона");
        authServiceStep.signInE2eStep(admin);
        ResponseGetUsers responseGetUsers = getUsersStep();
        deleteUserByPhone(phoneNumber, responseGetUsers);
    }

    /*
     * Метод находит и удаляет пользователя по его номеру телефона
     */
    @Step("Step | Удаление пользователя по номеру телефона")
    public void deleteUserByPhone(String phoneNumber, ResponseGetUsers responseGetUsers) {
        log.info("Step | Удаление пользователя с номером телефона {}", phoneNumber);
        ResponseGetUsers.Data.Content user = getUserByPhone(phoneNumber, responseGetUsers);
        if (user != null) {
            adminController.deleteUser(user.getId());
        } else {
            log.error(String.format("Пользователь с phoneNumber=[%s] не найден", phoneNumber));
        }
    }

    public ResponseGetUsers.Data.Content getUserByPhone(String phoneNumber, ResponseGetUsers response) {
        List<ResponseGetUsers.Data.Content> users = response.getData().getContent();
        return users.stream().filter(u -> u.getPhoneNumber().equals(phoneNumber)).findFirst().orElse(null);
    }
}
