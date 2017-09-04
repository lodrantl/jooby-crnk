package si.lodrant.jooby.crnk.internal;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import io.crnk.core.engine.properties.PropertiesProvider;


public class JoobyPropertiesProvider implements PropertiesProvider {
    private final Config config;

    public JoobyPropertiesProvider(Config config) {
        this.config = config;
    }

    @Override
    public String getProperty(String key) {
        try {
            return config.getString(key);
        } catch (ConfigException e) {
            return null;
        }
    }
}
