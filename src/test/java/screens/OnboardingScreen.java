package screens;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class OnboardingScreen {
    private SelenideElement element(String resourceId) {
        return $(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/" + resourceId + "\")"));
    }

    @Step("Проверить заголовок экрана onboarding: {title}")
    public OnboardingScreen checkTitle(String title) {
        element("primaryTextView").shouldBe(visible).shouldHave(text(title));
        return this;
    }

    @Step("Перейти к следующему экрану onboarding")
    public OnboardingScreen next() {
        element("fragment_onboarding_forward_button").click();
        return this;
    }

    @Step("Завершить onboarding")
    public OnboardingScreen finish() {
        element("(fragment_onboarding_done_button|rejectButton)").click();
        return this;
    }

    @Step("Проверить, что открылась главная страница Wikipedia")
    public void checkMainScreen() {
        element("search_container").shouldBe(visible);
    }

    @Step("Пропустить onboarding, если он отображается")
    public void skipIfPresent() {
        var skip = element("fragment_onboarding_skip_button");
        if (skip.exists()) skip.click();
    }
}
