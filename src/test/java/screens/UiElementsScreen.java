package screens;

import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.appium.java_client.AppiumBy.accessibilityId;

public class UiElementsScreen {
    private WebDriverWait waitForElement() {
        return new WebDriverWait(getWebDriver(), Duration.ofSeconds(30));
    }

    @Step("Проверить открытие экрана UI Elements")
    public UiElementsScreen checkOpened() {
        waitForElement().until(ExpectedConditions.visibilityOfElementLocated(accessibilityId("UI Elements")));
        return this;
    }

    @Step("Проверить доступность кнопок Text Button и Alert")
    public void checkControlsAvailable() {
        waitForElement().until(ExpectedConditions.elementToBeClickable(accessibilityId("Text Button")));
        waitForElement().until(ExpectedConditions.elementToBeClickable(accessibilityId("Alert")));
    }
}
