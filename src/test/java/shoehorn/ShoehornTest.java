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
    public void testShoehorn_UnmappedKey() throws Exception {
        createShoehornFixture().shoehorn();

        assertEquals("Unmapped key should not be shoe horned", null, System.getProperty("ORIGINAL_KEY_A"));
    }

    @Test
    public void testShoehorn_BareKey() throws Exception {
        createShoehornFixture().shoehorn();

        assertEquals("Bare key should be shoe horned as is", "ORIGINAL_VALUE_B", System.getProperty("ORIGINAL_KEY_B"));
    }

    @Test
    public void testShoehorn_MappedKey() throws Exception {
        createShoehornFixture().shoehorn();

        assertEquals("Mapped key should be shoe horned to new key", "ORIGINAL_VALUE_C", System.getProperty("NEW_KEY_C"));
        assertEquals("Mapped key should not be shoe horned to original key", null, System.getProperty("ORIGINAL_KEY_C"));
    }

    @Test
    public void testShoehorn_MultiMappedKey() throws Exception {
        createShoehornFixture().shoehorn();

        final String msg = "MultiMapped key should be shoe horned to all new keys";
        assertEquals(msg, "ORIGINAL_VALUE_D", System.getProperty("NEW_KEY_D1"));
        assertEquals(msg, "ORIGINAL_VALUE_D", System.getProperty("NEW_KEY_D2"));
        assertEquals("MultiMapped key should not be shoe horned to original key", null, System.getProperty("ORIGINAL_KEY_D"));
    }

    private static Shoehorn createShoehornFixture() {
        final Map<String, String> feet = ImmutableMap.of(
                "ORIGINAL_KEY_A", "ORIGINAL_VALUE_A",
                "ORIGINAL_KEY_B", "ORIGINAL_VALUE_B",
                "ORIGINAL_KEY_C", "ORIGINAL_VALUE_C",
                "ORIGINAL_KEY_D", "ORIGINAL_VALUE_D"
        );

        final Map<String, String> mappings = ImmutableMap.of(
                "ORIGINAL_KEY_B", "",
                "ORIGINAL_KEY_C", "NEW_KEY_C",
                "ORIGINAL_KEY_D", "NEW_KEY_D1 NEW_KEY_D2"
        );

        return new Shoehorn(feet, mappings);
    }
}
