package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.RealConfig;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public class RealDriver implements WebDriverProvider {
    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        RealConfig config = ConfigFactory.create(RealConfig.class, System.getProperties(), System.getenv());
        return LocalDriver.create(config, null);
    }
}
