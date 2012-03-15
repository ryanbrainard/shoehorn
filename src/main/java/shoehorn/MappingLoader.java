package shoehorn;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
abstract class MappingLoader {

    static Map<String, String> load(String mapArg) {
        final MappingLoader loader;
        if (new File(mapArg).exists()) {
            loader = new FileMappingLoader();
        } else {
            loader = new StringMappingLoader();
        }

        return loader.loadInternal(mapArg);
    }

    protected Map<String, String> loadInternal(String mapArg) {
        final Properties mappingsAsProps = new Properties();

        Closeable input = null;
        try {
            input = inject(mapArg, mappingsAsProps);
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

        return Collections.unmodifiableMap(mappings);
    }

    protected abstract Closeable inject(String args, Properties into) throws IOException;
}
