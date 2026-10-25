package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"file:real.local.properties", "classpath:config/real.properties"})
public interface RealConfig extends LocalConfig {
}
