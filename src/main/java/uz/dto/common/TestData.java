package uz.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestData {
    @Data
    @AllArgsConstructor
    public class User {
        private String phoneNumber;
        private String password;
        private String deviceId;
    }
}
