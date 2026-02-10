package test;
import com.codeborne.selenide.Selenide;
import data.DataHelper;
import data.SQLHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import page.LoginPage;

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
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
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
    @Test
    @DisplayName("Should get error notification if login with exist user and wrong password")
    void shouldGetErrorNotificationIfLoginWithExistUserAndWrongPassword() {
        var wrongPassword = DataHelper.generateWrongPassword(); // метод, возвращающий заведомо неверный пароль
        var wrongAuthInfo = new DataHelper.AuthInfo(authInfo.getLogin(), wrongPassword);

        loginPage.loginWithInvalidCredentials(wrongAuthInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }


}
