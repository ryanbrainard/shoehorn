package shoehorn;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
class MappingLoader {

    static Map<String, String> load(String mapArg) {
        final MappingLoader loader;
        if (new File(mapArg).exists()) {
            loader = new MappingLoader();
        } else {
            return Collections.emptyMap();
        }

        return loader.loadInternal(mapArg);
    }

    protected Map<String, String> loadInternal(String mapArg) {
        final Properties mappingsAsProps = new Properties();

        final File mapFile = new File(mapArg);

        InputStream mapFileStream = null;
        try {
            mapFileStream = new FileInputStream(mapFile);
            mappingsAsProps.load(mapFileStream);
            System.out.println("Loaded shoehorn mappings: " + mapArg);
        } catch (FileNotFoundException e) {
            System.err.println("Could not find shoehorn mappings: " + mapArg);
        } catch (IOException e) {
            System.err.println("Failed to load shoehorn mappings: " + mapArg);
        } finally {
            if (mapFileStream != null) {
                try {
                    mapFileStream.close();
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
}
