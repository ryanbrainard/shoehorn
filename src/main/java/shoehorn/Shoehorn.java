package shoehorn;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    public static void premain(String agentArgs) {
        new Shoehorn().shoehorn();
    }

    private void shoehorn() {
        System.out.println("Registering Environment Variables as Java System Properties");

        Properties mappings = getMappings("shoehorn.map");

        for (Map.Entry<String, String> envVar : System.getenv().entrySet()) {
            System.setProperty(envVar.getKey(), envVar.getValue());
            System.out.println("Loaded shoehorn mapping: " + envVar.getKey());
        }
    }

    Properties getMappings(String mapFilePath) {
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

        return mappings;
    }
}
