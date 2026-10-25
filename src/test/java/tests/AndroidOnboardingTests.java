package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.OnboardingScreen;

@Tag("android")
@Tag("onboarding")
public class AndroidOnboardingTests extends TestBase {
    @Test
    @DisplayName("Пройти четыре экрана знакомства с Wikipedia")
    void successfulOnboardingTest() {
        new OnboardingScreen()
                .checkTitle("The Free Encyclopedia")
                .next()
                .checkTitle("New ways to explore")
                .next()
                .checkTitle("Reading lists with sync")
                .next()
                .checkTitle("Send anonymous data")
                .finish()
                .checkMainScreen();
    }
}
