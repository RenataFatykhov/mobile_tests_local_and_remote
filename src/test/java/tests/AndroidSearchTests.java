package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import screens.SearchScreen;

@Tag("android")
public class AndroidSearchTests extends TestBase {
    @Test
    @DisplayName("Поиск Appium возвращает результаты")
    void successfulSearchTest() {
        new SearchScreen().searchFor("Appium").checkResultsFound();
    }

    @Test
    @DisplayName("Из поиска открывается статья Appium")
    void successfulOpenArticleTest() {
        new SearchScreen()
                .searchFor("Appium")
                .openArticle("Appium")
                .checkArticleOpened("Appium");
    }
}
