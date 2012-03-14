package shoehorn;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Ryan Brainard
 */
public class ShoehornTest {

    @Test
    public void testGetMappings() {
        final String mapFilePath = ClassLoader.getSystemResource("shoehorn.map.test").getPath();
        final Map<String, String> mappings = Shoehorn.getMappings(mapFilePath);

        assertTrue(mappings.containsKey("SOME_KEY"));
        assertEquals("SOME_VALUE", mappings.get("SOME_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY"));
        assertEquals("", mappings.get("BARE_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY_WITH_EQUALS"));
        assertEquals("", mappings.get("BARE_KEY_WITH_EQUALS"));

        assertFalse(mappings.containsKey("MISSING_KEY"));
        assertEquals(null, mappings.get("MISSING_KEY"));
    }

    @Test
    public void testGetMappings_BadFileName() {
        final String mapFilePath = "bad-file-name";
        final Map<String, String> mappings = Shoehorn.getMappings(mapFilePath);

        assertTrue(mappings.isEmpty());
    }

    @Test
    public void testShoehorn() throws Exception {
        final Map<String, String> feet = ImmutableMap.of("ORIGINAL_KEY_A", "ORIGINAL_VALUE_A",
                "ORIGINAL_KEY_B", "ORIGINAL_VALUE_B",
                "ORIGINAL_KEY_C", "ORIGINAL_VALUE_C");

        final Map<String, String> mappings = ImmutableMap.of("ORIGINAL_KEY_B", "",
                "ORIGINAL_KEY_C", "NEW_KEY_C");

        final Shoehorn shoehorn = new Shoehorn(feet, mappings);

        shoehorn.shoehorn();

        assertEquals("Unmapped key should not be shoe horned", null, System.getProperty("ORIGINAL_KEY_A"));
        assertEquals("Bare key should be shoe horned as is", "ORIGINAL_VALUE_B", System.getProperty("ORIGINAL_KEY_B"));
        assertEquals("Mapped key should be shoe horned to new key", "ORIGINAL_VALUE_C", System.getProperty("NEW_KEY_C"));
        assertEquals("Mapped key should be shoe horned to new key", null, System.getProperty("ORIGINAL_KEY_C"));
    }
}
