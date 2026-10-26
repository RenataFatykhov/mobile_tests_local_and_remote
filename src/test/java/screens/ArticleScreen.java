package screens;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static io.appium.java_client.AppiumBy.className;
import static io.appium.java_client.AppiumBy.xpath;

public class ArticleScreen {
    @Step("Проверить открытие статьи: {title}")
    public void checkArticleOpened(String title) {
        $(className("android.webkit.WebView")).shouldBe(visible)
                .$$(xpath(".//*[@text]"))
                .findBy(exactText(title)).shouldBe(visible);
    }
}
