package drivers;

import com.codeborne.selenide.WebDriverProvider;
import config.BrowserstackConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BrowserstackDriver implements WebDriverProvider {
    private final BrowserstackConfig config = ConfigFactory.create(
            BrowserstackConfig.class,
            System.getProperties(),
            System.getenv()
    );

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        String platform = config.platform().toLowerCase(Locale.ROOT);
        boolean isAndroid = platform.equals("android");

        if (!isAndroid && !platform.equals("ios")) {
            throw new IllegalArgumentException("Unsupported platform: " + config.platform());
        }

        MutableCapabilities caps = new MutableCapabilities();
        Map<String, Object> browserStackOptions = new HashMap<>();

        browserStackOptions.put("userName", config.browserStackUser());
        browserStackOptions.put("accessKey", config.browserStackKey());
        browserStackOptions.put("deviceName", isAndroid ? config.androidDeviceName() : config.iosDeviceName());
        browserStackOptions.put("osVersion", isAndroid ? config.androidOsVersion() : config.iosOsVersion());
        browserStackOptions.put("appiumVersion", config.appiumVersion());
        browserStackOptions.put("projectName", config.projectName());
        browserStackOptions.put("buildName", config.buildName());
        browserStackOptions.put("sessionName", config.sessionName());

        caps.setCapability("platformName", platform);
        caps.setCapability("appium:app", isAndroid ? config.androidApp() : config.iosApp());
        caps.setCapability("appium:automationName",
                isAndroid ? config.androidAutomationName() : config.iosAutomationName());
        caps.setCapability("appium:noReset", false);
        caps.setCapability("appium:language", "en");
        caps.setCapability("appium:locale", isAndroid ? "US" : "en_US");
        caps.setCapability("bstack:options", browserStackOptions);

        try {
            return isAndroid
                    ? new AndroidDriver(new URL(config.browserStackUrl()), caps)
                    : new IOSDriver(new URL(config.browserStackUrl()), caps);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("BrowserStack URL is invalid", e);
        }
    }
}
