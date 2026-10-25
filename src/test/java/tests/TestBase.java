package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.DriverFactory;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class TestBase {
    @BeforeAll
    static void beforeAll() {
        Configuration.browser = DriverFactory.driverClass();
        Configuration.browserSize = null;
        Configuration.timeout = 30000;
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        open();
        if (testInfo.getTags().contains("android") && !testInfo.getTags().contains("onboarding")) {
            new screens.OnboardingScreen().skipIfPresent();
        }
    }

    @AfterEach
    void addAttachments() {
        if (!WebDriverRunner.hasWebDriverStarted()) return;
        String sessionId = Selenide.sessionId().toString();
        try {
            try {
                Attach.screenshotAs("Last screenshot");
                Attach.pageSource();
            } catch (RuntimeException e) {
                Attach.attachAsText("Attachment error", e.getClass().getSimpleName());
            }
        } finally {
            closeWebDriver();
        }
        if (DriverFactory.isBrowserstack()) {
            try {
                Attach.addVideo(sessionId);
            } catch (RuntimeException | AssertionError e) {
                Attach.attachAsText("BrowserStack video unavailable", e.getClass().getSimpleName());
            }
        }
    }
}
