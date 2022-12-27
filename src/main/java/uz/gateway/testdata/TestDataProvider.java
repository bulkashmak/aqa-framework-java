package uz.gateway.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.gateway.GatewayContainer;
import uz.gateway.dto.users.admin.users.response.GetUsersResponse;
import uz.gateway.testdata.pojo.Client;
import uz.gateway.testdata.pojo.Server;
import uz.gateway.testdata.pojo.TestData;
import uz.gateway.testdata.pojo.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
 * Класс для работы с тестовыми данными в resources
 */
@Slf4j
@Component
public class TestDataProvider {

    @Autowired
    GatewayContainer gatewayContainer;

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
            gatewayContainer.setUser(user);
            return user;
        }
    }

    public List<User> getUsers() {
        return testData.getUsers();
    }

    public GetUsersResponse.Data.Content getUserByPhone(String phoneNumber, GetUsersResponse response) {
        List<GetUsersResponse.Data.Content> users = response.getData().getContent();
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