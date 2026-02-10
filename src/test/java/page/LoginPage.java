package page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'] .notification__content");

    public void verifyErrorNotification(String expectedText) {
        errorNotification
                .shouldBe(com.codeborne.selenide.Condition.visible)
                .shouldHave(com.codeborne.selenide.Condition.text(expectedText));
    }

    public VerificationPage login(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public LoginPage loginWithInvalidCredentials(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return this;
    }


}