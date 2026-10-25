package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "file:browserstack.local.properties",
        "classpath:config/browserstack.properties"
})
public interface BrowserstackConfig extends Config {

    @Key("platform")
    @DefaultValue("android")
    String platform();

    @Key("BROWSERSTACK_USER")
    String browserStackUser();

    @Key("BROWSERSTACK_KEY")
    String browserStackKey();

    @Key("browserstack.url")
    String browserStackUrl();

    @Key("browserstack.project")
    String projectName();

    @Key("browserstack.build")
    String buildName();

    @Key("browserstack.session")
    String sessionName();

    @Key("browserstack.appium.version")
    String appiumVersion();

    @Key("BROWSERSTACK_ANDROID_DEVICE")
    String androidDeviceName();

    @Key("BROWSERSTACK_ANDROID_OS_VERSION")
    String androidOsVersion();

    @Key("android.app")
    String androidApp();

    @Key("android.automation.name")
    String androidAutomationName();

    @Key("BROWSERSTACK_IOS_DEVICE")
    String iosDeviceName();

    @Key("BROWSERSTACK_IOS_OS_VERSION")
    String iosOsVersion();

    @Key("ios.app")
    String iosApp();

    @Key("ios.automation.name")
    String iosAutomationName();
}
