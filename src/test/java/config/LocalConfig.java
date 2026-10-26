package config;

import org.aeonbits.owner.Config;

public interface LocalConfig extends Config {
    @Key("appium.url") String appiumUrl();
    @Key("device.name") String deviceName();
    @Key("device.udid") String udid();
    @Key("android.app") String app();
    @Key("android.app.url") String appUrl();
    @Key("android.app.package") String appPackage();
    @Key("android.app.activity") String appActivity();
    @Key("appium.systemPort") int systemPort();
}
