package drivers;

import config.LocalConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipFile;

final class LocalDriver {
    private LocalDriver() {}

    static WebDriver create(LocalConfig config, String avd) {
        if (!"android".equals(System.getProperty("platform", "android"))) {
            throw new IllegalArgumentException("Local drivers support only -Dplatform=android");
        }
        Path apk = getAppPath(config.app(), config.appUrl());
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

    static synchronized Path getAppPath(String appPath, String appUrl) {
        Path apk = Path.of(appPath).toAbsolutePath();
        Path temporaryFile = null;
        try {
            if (Files.isRegularFile(apk) && Files.size(apk) > 0) {
                return apk;
            }
            Files.createDirectories(apk.getParent());
            temporaryFile = Files.createTempFile(apk.getParent(), "wikipedia-", ".part");
            URLConnection connection = new URL(appUrl).openConnection();
            connection.setRequestProperty("User-Agent", "MobileTests/1.0 (QA.GURU training project)");
            connection.setConnectTimeout(30_000);
            connection.setReadTimeout(60_000);
            try (InputStream input = connection.getInputStream()) {
                Files.copy(input, temporaryFile, StandardCopyOption.REPLACE_EXISTING);
            }
            // Do not cache an HTML error page or an incomplete APK.
            try (ZipFile archive = new ZipFile(temporaryFile.toFile())) {
                if (archive.getEntry("AndroidManifest.xml") == null) {
                    throw new IOException("Downloaded file is not an Android APK");
                }
            }
            Files.move(temporaryFile, apk, StandardCopyOption.REPLACE_EXISTING);
            return apk;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to download APK from " + appUrl + " to " + apk, e);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException e) {
                    temporaryFile.toFile().deleteOnExit();
                }
            }
        }
    }

}
