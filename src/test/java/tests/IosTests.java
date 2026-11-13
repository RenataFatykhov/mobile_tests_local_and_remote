package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import screens.UiElementsScreen;

@Tag("ios")
public class IosTests extends TestBase {
    @Test
    @DisplayName("В UI Elements доступны кнопки Text Button и Alert")
    void successfulOpenUiElementsScreenTest() {
        new UiElementsScreen().checkOpened().checkControlsAvailable();
    }
}
