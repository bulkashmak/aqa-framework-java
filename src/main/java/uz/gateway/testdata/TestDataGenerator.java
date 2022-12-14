package uz.gateway.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import uz.gateway.testdata.pojo.TestData;
import uz.gateway.testdata.pojo.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
 * Класс для работы с тестовыми данными в resources
 */
public class TestDataGenerator {

    /*
     * Метод находит и возвращает пользователя по полю 'alias'
     */
    public User getUserByAlias(String alias) {
        User testUser = getUsers().stream()
                .filter(user -> user.getAlias().equals(alias)).findFirst().orElse(null);
        if (testUser == null) {
            throw new RuntimeException("Ошибка при чтении user из testdata");
        } else {
            return testUser;
        }
    }

    public User getUserById(int userId) {
        return readTestData().getUsers().get(userId);
    }

    public List<User> getUsers() {
        return readTestData().getUsers();
    }

    /*
     * Метод читает файл resources/testdata.json и сериализует его в объект TestData
     */
    public TestData readTestData() {
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