package screens;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.appium.java_client.AppiumBy.accessibilityId;
import static io.appium.java_client.AppiumBy.androidUIAutomator;

public class SearchScreen {
    @Step("Найти в Wikipedia: {query}")
    public SearchScreen searchFor(String query) {
        $(accessibilityId("Search Wikipedia")).click();
        $(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/search_src_text\")"))
                .sendKeys(query);
        return this;
    }

    @Step("Проверить наличие результатов поиска")
    public SearchScreen checkResultsFound() {
        $$(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/page_list_item_title\")"))
                .shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Открыть статью: {title}")
    public ArticleScreen openArticle(String title) {
        $$(androidUIAutomator("new UiSelector().resourceIdMatches(\".*:id/page_list_item_title\")"))
                .findBy(exactText(title)).click();
        return new ArticleScreen();
    }
}
