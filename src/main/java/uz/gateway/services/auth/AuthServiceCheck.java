package uz.gateway.services.auth;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.gateway.GatewayContext;
import uz.gateway.dto.users.admin.users.response.GetUsersResponse;
import uz.gateway.services.users.UsersServiceStep;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Component
public class AuthServiceCheck {

    @Autowired
    GatewayContext gatewayContext;

    @Autowired
    AuthServiceStep authServiceStep;

    @Autowired
    UsersServiceStep usersServiceStep;

    @Step("CHECK | Пользователь создан в БД")
    public void userCreatedCheck() {
        log.info("CHECK | Пользователь создан в БД");
        authServiceStep.signInAdminE2eStep();
        GetUsersResponse getUsersResponse = usersServiceStep.getUsersStep();
        assertNotNull(
                usersServiceStep.getUserByPhone(gatewayContext.getUser().getPhoneNumber(), getUsersResponse),
                String.format("Пользователь с номером телефона %s не найден в списке пользователей",
                        gatewayContext.getUser().getPhoneNumber()));
    }

    public void resetPasswordCheck() {
        authServiceStep.signInE2eStep();
    }
}
