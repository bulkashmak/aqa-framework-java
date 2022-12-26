package uz.gateway.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.gateway.dto.auth.signIn.request.RequestSignInVerify;
import uz.gateway.dto.auth.signIn.response.ResponseSignIn;
import uz.gateway.dto.auth.signIn.response.ResponseSignInVerify;
import uz.gateway.dto.users.admin.users.response.ResponseGetUsers;
import uz.gateway.services.auth.AuthService;
import uz.gateway.services.users.controllers.AdminController;
import uz.gateway.testdata.pojo.Client;
import uz.gateway.testdata.pojo.Server;
import uz.gateway.testdata.pojo.TestData;
import uz.gateway.testdata.pojo.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;

/*
 * Класс для работы с тестовыми данными в resources
 */
@Slf4j
@Component
public class TestDataProvider {

    @Autowired
    AuthService authService;

    @Autowired
    AdminController adminController;

    static TestData testData = readTestData();

    public String getClientURI(String serverAlias, String clientAlias) {
        return String.format("%s:%s",
                getServerByAlias(serverAlias).getUri(), getClientByAlias(clientAlias).getPort());
    }

    /*
     * Метод находит и возвращает server по полю 'alias'
     */
    public Server getServerByAlias(String alias) {
        Server server = testData.getServers().stream()
                .filter(s -> s.getAlias().equals(alias)).findFirst().orElse(null);
        if (server == null) {
            throw new RuntimeException("Ошибка при чтении server из testdata");
        } else {
            return server;
        }
    }

    /*
     * Метод находит и возвращает client по полю 'alias'
     */
    public Client getClientByAlias(String alias) {
        Client client = testData.getClients().stream()
                .filter(c -> c.getAlias().equals(alias)).findFirst().orElse(null);
        if (client == null) {
            throw new RuntimeException("Ошибка при чтении client из testdata");
        } else {
            return client;
        }
    }

    /*
     * Метод находит и возвращает user по полю 'alias'
     */
    public User getUserByAlias(String alias) {
        User user = getUsers().stream()
                .filter(u -> u.getAlias().equals(alias)).findFirst().orElse(null);
        if (user == null) {
            throw new RuntimeException("Ошибка при чтении user из testdata");
        } else {
            return user;
        }
    }

    public List<User> getUsers() {
        return testData.getUsers();
    }

    /*
     * Метод находит и удаляет пользователя по его номеру телефона
     */
    public void deleteUserByPhone(String phoneNumber) {
        log.info("[PRECONDITION] Удаление пользователя с номером телефона {}", phoneNumber);
        User admin = getUserByAlias("admin");
        ResponseSignIn responseSignIn = authService.postSignIn(
                        admin.getPhoneNumber(), admin.getPassword(), admin.getDeviceId())
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignIn.class);
        ResponseSignInVerify responseSignInVerify = authService.postSignInVerify(new RequestSignInVerify(
                        admin.getDeviceId(),
                        responseSignIn.getData().getConfirmationKey(),
                        admin.getOtp()))
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract().as(ResponseSignInVerify.class);

        ResponseGetUsers responseGetUsers = adminController.getUsers(responseSignInVerify.getData().getAccessToken())
                .statusCode(SC_OK)
                .extract().as(ResponseGetUsers.class);

        ResponseGetUsers.Data.Content user = getUserByPhone(phoneNumber, responseGetUsers);
        if (user != null) {
            adminController.deleteUser(responseSignInVerify.getData().getAccessToken(), user.getId());
        } else {
            log.error(String.format("Пользователь с phoneNumber=[%s] не найден", phoneNumber));
        }
    }

    private static ResponseGetUsers.Data.Content getUserByPhone(String phoneNumber, ResponseGetUsers response) {
        List<ResponseGetUsers.Data.Content> users = response.getData().getContent();
        return users.stream().filter(u -> u.getPhoneNumber().equals(phoneNumber)).findFirst().orElse(null);
    }

    /*
     * Метод читает файл resources/testdata.json и сериализует его в объект TestData
     */
    private static TestData readTestData() {
        ObjectMapper objectMapper = new ObjectMapper();
        TestData testData;
        try {
            testData = objectMapper.readValue(new File("src/main/resources/testdata.json"), TestData.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации тестовых данных", e);
        }
        return testData;
    }
}