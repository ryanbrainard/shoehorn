package shoehorn;

import com.google.common.collect.Maps;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

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
        new Shoehorn(System.getenv(), getMappings("shoehorn.map")).shoehorn();
    }

    static Map<String, String> getMappings(String mapFilePath) {
        final Properties mappings = new Properties();
        final File mapFile = new File(mapFilePath);

        InputStream mapFileStream = null;
        try {
            mapFileStream = new FileInputStream(mapFile);
            mappings.load(mapFileStream);
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

        return Maps.fromProperties(mappings);
    }
}
