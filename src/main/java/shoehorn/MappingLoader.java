package shoehorn;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
class MappingLoader {

    public static final String SHOEHORN_MAP_ENV_VAR_PREFIX = "SHOEHORN_MAP";

    public Map<String, String> load(Map<String, String> environment) {
        final Map<String, String> mappings = new HashMap<String, String>();

        for (Map.Entry<String, String> env : environment.entrySet()) {
            if (env.getKey().startsWith(SHOEHORN_MAP_ENV_VAR_PREFIX)) {
                loadOne(mappings, env.getValue());
            }
        }

        return Collections.unmodifiableMap(mappings);
    }

    private void loadOne(Map<String, String> mappings, String mapping) {
        if (mapping == null) {
            return;
        }

        final Properties mappingsAsProps = new Properties();

        Reader input = null;
        try {
            input = new StringReader(mapping.replaceAll(";", "\n"));
            mappingsAsProps.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load shoehorn mapping from: " + mapping);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        for (Map.Entry<Object, Object> prop : mappingsAsProps.entrySet()) {
            mappings.put(prop.getKey().toString(), prop.getValue().toString());
        }
    }
}
