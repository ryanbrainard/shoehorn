package shoehorn;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Ryan Brainard
 */
public class MappingLoaderTest {

    @Test
    public void testGetMappings_FromFile() {
        final String mapFilePath = ClassLoader.getSystemResource("shoehorn.map.test").getPath();
        final Map<String, String> mappings = MappingLoader.load(mapFilePath);

        assertMappings(mappings);
    }

    @Test
    public void testGetMappings_FromArg() {
        final Map<String, String> mappings = MappingLoader.load("SOME_KEY=SOME_VALUE;BARE_KEY;BARE_KEY_WITH_EQUALS=");

        assertMappings(mappings);
    }

    private void assertMappings(Map<String, String> mappings) {
        assertTrue(mappings.containsKey("SOME_KEY"));
        assertEquals("SOME_VALUE", mappings.get("SOME_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY"));
        assertEquals("", mappings.get("BARE_KEY"));

        assertTrue(mappings.containsKey("BARE_KEY_WITH_EQUALS"));
        assertEquals("", mappings.get("BARE_KEY_WITH_EQUALS"));

        assertFalse(mappings.containsKey("MISSING_KEY"));
        assertEquals(null, mappings.get("MISSING_KEY"));
    }
}
