package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:emulation.local.properties", "classpath:config/emulation.properties"})
public interface EmulationConfig extends LocalConfig {
    @Key("device.avd") String avd();
}
