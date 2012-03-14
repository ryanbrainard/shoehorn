package shoehorn;

import com.google.common.collect.Maps;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    private final Map<String, String> foot;
    private final Map<String, String> mappings;

    public Shoehorn(Map<String, String> foot, Map<String, String> mappings) {
        this.foot = foot;
        this.mappings = mappings;
    }

    void shoehorn() {
        for (Map.Entry<String, String> envVar : foot.entrySet()) {
            System.setProperty(envVar.getKey(), envVar.getValue());
            System.out.println("Loaded shoehorn mapping: " + envVar.getKey());
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
