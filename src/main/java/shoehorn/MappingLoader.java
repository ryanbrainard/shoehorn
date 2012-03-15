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

    public Map<String, String> load(Map<String, String> environment, String overrides) {
        final Map<String, String> mappings = new HashMap<String, String>();

        for (Map.Entry<String, String> env : environment.entrySet()) {
            if (env.getKey().startsWith(SHOEHORN_MAP_ENV_VAR_PREFIX)) {
                mappings.putAll(loadOne(env.getValue()));
            }
        }

        mappings.putAll(loadOne(overrides));

        return Collections.unmodifiableMap(mappings);
    }

    private Map<String, String> loadOne(String mapArg) {
        if (mapArg == null) {
            return Collections.emptyMap();
        }

        System.out.println("Loading shoehorn mappings with " + this.getClass().getSimpleName());

        final Properties mappingsAsProps = new Properties();

        Reader input = null;
        try {
            input = new StringReader(mapArg.replaceAll(";", "\n"));
            mappingsAsProps.load(input);
            System.out.println("Loaded shoehorn mappings: " + mapArg);
        } catch (IOException e) {
            System.err.println("Failed to load shoehorn mappings from: " + mapArg);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        final Map<String, String> mappings = new HashMap<String, String>();
        for (Map.Entry<Object, Object> prop : mappingsAsProps.entrySet()) {
            mappings.put(prop.getKey().toString(), prop.getValue().toString());
        }

        return mappings;
    }
}
