package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id=code] input"); // 2 usages
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]"); // 1 usage
    private final SelenideElement errorNotification = $("[data-test-id='error-notification'].notification__content"); // 1 usage

    public VerificationPage() {
        codeField.shouldBe(visible);
    }

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        verify(verificationCode);
        return new DashboardPage();
    }

    public void verify(String verificationCode) { // 2 usages
        codeField.setValue(verificationCode);
        verifyButton.click();
    }
}
