package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.appium.java_client.AppiumBy.*;
import static io.qameta.allure.Allure.step;

@Tag("android")
public class AndroidSearchTests extends TestBase {

    @Test
    void successfulSearchTest() {

        step("Type search", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/search_src_text\")")).sendKeys("Appium");

        });

        step("Verify content found", () -> {
            $$(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/page_list_item_title\")")).shouldHave(sizeGreaterThan(0));
        });

    }

    @Test
    void successfulOpenArticleTest() {
        step("Search for the Appium article", () -> {
            $(accessibilityId("Search Wikipedia")).click();
            $(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/search_src_text\")")).sendKeys("Appium");
        });

        step("Open the Appium article", () ->
                $(androidUIAutomator("new UiSelector().text(\"Appium\")")).click());

        step("Verify that the article is open", () ->
                $(androidUIAutomator("new UiSelector().textContains(\"Appium\")")).shouldBe(visible));
    }
}
