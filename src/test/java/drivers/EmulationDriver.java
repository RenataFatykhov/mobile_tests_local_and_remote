package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.EmulationConfig;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public class EmulationDriver implements WebDriverProvider {
    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        EmulationConfig config = ConfigFactory.create(EmulationConfig.class, System.getProperties(), System.getenv());
        return LocalDriver.create(config, config.avd());
    }
}
