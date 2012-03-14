package shoehorn;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    public static final String DEFAULT_MAP_FILE_PATH = "shoehorn.map";
    private final Map<String, String> feet;
    private final Map<String, String> mappings;

    public Shoehorn(Map<String, String> feet, Map<String, String> mappings) {
        this.feet = feet;
        this.mappings = mappings;
    }

    void shoehorn() {
        for (Map.Entry<String, String> foot : feet.entrySet()) {
            if (!mappings.containsKey(foot.getKey())) {
                continue;
            }

            final String shoeKey;
            if (mappings.get(foot.getKey()).trim().equals("")) {
                shoeKey = foot.getKey();
            } else {
                shoeKey = mappings.get(foot.getKey());
            }

            System.setProperty(shoeKey, foot.getValue());
            System.out.println("Shoehorned [" + foot.getKey() + "] into [" + shoeKey + "]");
        }
    }

    public static void premain(String agentArgs) {
        final String mapFilePath = agentArgs != null ? agentArgs : DEFAULT_MAP_FILE_PATH;
        new Shoehorn(System.getenv(), getMappings(mapFilePath)).shoehorn();
    }

    static Map<String, String> getMappings(String mapFilePath) {
        final Properties mappingsAsProps = new Properties();
        final File mapFile = new File(mapFilePath);

        InputStream mapFileStream = null;
        try {
            mapFileStream = new FileInputStream(mapFile);
            mappingsAsProps.load(mapFileStream);
            System.out.println("Loaded shoehorn mappings: " + mapFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Could not find shoehorn mappings: " + mapFilePath);
        } catch (IOException e) {
            System.err.println("Failed to load shoehorn mappings: " + mapFilePath);
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

        return mappings;
    }
}
