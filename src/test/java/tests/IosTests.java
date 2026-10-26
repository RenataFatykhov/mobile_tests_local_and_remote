package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.UiElementsScreen;

@Tag("ios")
public class IosTests extends TestBase {
    @Test
    void successfulOpenUiElementsScreenTest() {
        new UiElementsScreen().checkOpened().checkControlsAvailable();
    }
}
