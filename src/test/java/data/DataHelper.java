package data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Locale;

public class DataHelper { // 10 usages
    private static final Faker FAKER = new Faker(new Locale("en")); // 3 usages

    private DataHelper() { // no usages
    }

    public static AuthInfo getAuthInfoWithTestData() {
        return new AuthInfo("vasya", "qwerty123");
    }

    private static String generateRandomLogin() {
        return FAKER.name().username();
    }

    private static String generateRandomPassword() {
        return FAKER.internet().password();
    }

    public static AuthInfo generateRandomUser() { // 1 usage
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static VerificationCode generateRandomVerificationCode() { // 1 usage
        return new VerificationCode(FAKER.numerify("#######"));
    }

    @Value // 7 usages
    public static class AuthInfo {
        String login;
        String password;
    }

    @Data // 4 usages
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationCode {
        String code;
    }
}
