package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.qameta.allure.Allure.step;

@Tag("ios")
public class IosTests extends TestBase {

    @Test
    void successfulOpenUiElementsScreenTest() {
        WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(30));

        step("Verify the UI Elements screen is open", () ->
                wait.until(ExpectedConditions.visibilityOfElementLocated(accessibilityId("UI Elements"))));

        step("Verify the text and alert controls are available", () -> {
            wait.until(ExpectedConditions.elementToBeClickable(accessibilityId("Text Button")));
            wait.until(ExpectedConditions.elementToBeClickable(accessibilityId("Alert")));
        });
    }
}
