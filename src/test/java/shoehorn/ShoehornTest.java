package shoehorn;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Ryan Brainard
 */
public class ShoehornTest {

    @Test
    public void testGetMappings() {
        final String mapFilePath = ClassLoader.getSystemResource("shoehorn.map.test").getPath();
        final Properties mappings = Shoehorn.getMappings(mapFilePath);

        assertTrue(mappings.containsKey("SOME_KEY"));
        assertEquals("SOME_VALUE", mappings.getProperty("SOME_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY"));
        assertEquals("", mappings.getProperty("BARE_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY_WITH_EQUALS"));
        assertEquals("", mappings.getProperty("BARE_KEY_WITH_EQUALS"));

        assertFalse(mappings.containsKey("MISSING_KEY"));
        assertEquals(null, mappings.getProperty("MISSING_KEY"));
    }

    @Test
    public void testGetMappings_BadFileName() {
        final String mapFilePath = "bad-file-name";
        final Properties mappings = Shoehorn.getMappings(mapFilePath);

        assertTrue(mappings.isEmpty());
    }

    @Test
    public void testShoehorn() throws Exception {
        Shoehorn shoehorn = createShoehorn("SOME_KEY", "SOME_VALUE");

        shoehorn.shoehorn();
    }

    private Shoehorn createShoehorn(String key, String value) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);

        return createShoehorn(map);
    }

    private Shoehorn createShoehorn(Map<String, String> map) {
        final Properties mappings = new Properties();
        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            mappings.setProperty(mapEntry.getKey(), mapEntry.getValue());
        }

        return new Shoehorn(System.getenv(), mappings);
    }
}
