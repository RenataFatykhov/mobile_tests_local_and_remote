package drivers;

public final class DriverFactory {
    private DriverFactory() {}

    public static String driverClass() {
        return switch (System.getProperty("deviceHost", "emulation")) {
            case "browserstack" -> BrowserstackDriver.class.getName();
            case "emulation" -> EmulationDriver.class.getName();
            case "real" -> RealDriver.class.getName();
            default -> throw new IllegalArgumentException(
                    "Unsupported deviceHost. Use browserstack, emulation or real");
        };
    }

    public static boolean isBrowserstack() {
        return "browserstack".equals(System.getProperty("deviceHost", "emulation"));
    }
}
