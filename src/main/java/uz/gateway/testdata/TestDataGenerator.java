package uz.gateway.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class TestDataGenerator {

    static TestData testData = readTestData();

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
     * Метод читает файл resources/testdata.json и сериализует его в объект TestData
     */
    public static TestData readTestData() {
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