package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.SearchScreen;

@Tag("android")
public class AndroidSearchTests extends TestBase {
    @Test
    void successfulSearchTest() {
        new SearchScreen().searchFor("Appium").checkResultsFound();
    }

    @Test
    void successfulOpenArticleTest() {
        new SearchScreen()
                .searchFor("Appium")
                .openArticle("Appium")
                .checkArticleOpened("Appium");
    }
}
