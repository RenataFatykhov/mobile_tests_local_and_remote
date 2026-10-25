package drivers;

import config.LocalConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

final class LocalDriver {
    private LocalDriver() {}

    static WebDriver create(LocalConfig config, String avd) {
        if (!"android".equals(System.getProperty("platform", "android"))) {
            throw new IllegalArgumentException("Local drivers support only -Dplatform=android");
        }
        Path apk = Path.of(config.app()).toAbsolutePath();
        if (!Files.isRegularFile(apk)) {
            throw new IllegalArgumentException("APK not found: " + apk + ". Set -Dandroid.app=/path/to/wikipedia.apk");
        }
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(config.deviceName())
                .setApp(apk.toString())
                .setAppPackage(config.appPackage())
                .setAppActivity(config.appActivity())
                .setLanguage("en")
                .setLocale("US")
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setSystemPort(config.systemPort());
        if (config.udid() != null && !config.udid().isBlank()) {
            options.setUdid(config.udid());
        } else if (avd == null || avd.isBlank()) {
            throw new IllegalArgumentException("Set -Ddevice.udid=<serial from adb devices> for a real device");
        }
        if (avd != null && !avd.isBlank()) {
            options.setAvd(avd);
        }
        try {
            return new AndroidDriver(new URL(config.appiumUrl()), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid appium.url: " + config.appiumUrl(), e);
        }
    }
}
