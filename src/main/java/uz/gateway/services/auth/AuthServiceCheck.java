package uz.gateway.services.auth;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.gateway.GatewayContainer;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;
import uz.gateway.services.users.UsersServiceStep;
import uz.gateway.testdata.pojo.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Component
public class AuthServiceCheck {

    @Autowired
    GatewayContainer gatewayContainer;

    @Autowired
    AuthServiceStep authServiceStep;

    @Autowired
    UsersServiceStep usersServiceStep;

    @Step("CHECK | Пользователь создан в БД")
    public void userCreatedCheck(User expectedUser, User admin) {
        log.info("CHECK | Пользователь создан в БД");
        authServiceStep.signInE2eStep(admin);
        ResponseGetUsers responseGetUsers = usersServiceStep.getUsersStep();
        assertNotNull(usersServiceStep.getUserByPhone(expectedUser.getPhoneNumber(), responseGetUsers),
                String.format("Пользователь с номером телефона %s не найден в списке пользователей",
                        expectedUser.getPhoneNumber()));
    }
}
