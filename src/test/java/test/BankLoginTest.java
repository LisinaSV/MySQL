package test;

import com.github.javafaker.Faker;
import data.DataHelper;
import data.SQLHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import page.LoginPage;


import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.cleanAuthCodes;
import static data.SQLHelper.cleanDatabase;

public class BankLoginTest {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        insertTestUsers();
    }

    @SneakyThrows
    private void insertTestUsers() {
        var countSQL = "SELECT COUNT(*) FROM users;";
        var cardsSQL = "SELECT id, number, balance_in_kopecks FROM cards WHERE user_id = ?;";

        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
             var countStmt = conn.createStatement();
             var cardsStmt = conn.prepareStatement(cardsSQL)) {
            try (var rs = countStmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    var count = rs.getInt("COUNT(*)");
                }
            }
            cardsStmt.setInt(1, 1);
            try (var rs = cardsStmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var number = rs.getString("number");
                    var balanceInKopecks = rs.getInt("balance_in_kopecks");

                }
            }
        }
    }


    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var verificationPage = loginPage.login(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationIFLoginWithExistUserAndRandomVerificationCode() {
        var verificationPage = loginPage.login(authInfo);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}
