package uz.gateway.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import uz.gateway.testdata.pojo.TestData;
import uz.gateway.testdata.pojo.User;

import java.io.File;
import java.io.IOException;

/*
 * Класс для работы с тестовыми данными в resources
 */
public class TestDataGenerator {
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

    public User getUser(int userId) {
        return readTestData().getUsers().get(userId);
    }
}