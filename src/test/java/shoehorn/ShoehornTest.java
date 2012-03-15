package shoehorn;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Ryan Brainard
 */
public class ShoehornTest {

    @Test
    public void testShoehorn_UnmappedKey() throws Exception {
        createShoehornFixture(ImmutableMap.of("KEY", "VALUE")).shoehorn();

        assertEquals("Unmapped key should not be shoe horned", null, System.getProperty("ORIGINAL_KEY_A"));
    }

    @Test
    public void testShoehorn_BareKey() throws Exception {
        createShoehornFixture(ImmutableMap.of("ORIGINAL_KEY_B", "")).shoehorn();

        assertEquals("Bare key should be shoe horned as is", "ORIGINAL_VALUE_B", System.getProperty("ORIGINAL_KEY_B"));
    }

    @Test
    public void testShoehorn_MappedKey() throws Exception {
        createShoehornFixture(ImmutableMap.of("ORIGINAL_KEY_C", "NEW_KEY_C")).shoehorn();

        assertEquals("Mapped key should be shoe horned to new key", "ORIGINAL_VALUE_C", System.getProperty("NEW_KEY_C"));
        assertEquals("Mapped key should not be shoe horned to original key", null, System.getProperty("ORIGINAL_KEY_C"));
    }

    @Test
    public void testShoehorn_MultiMappedKey() throws Exception {
        createShoehornFixture(ImmutableMap.of("ORIGINAL_KEY_D", "NEW_KEY_D1 NEW_KEY_D2")).shoehorn();

        final String msg = "MultiMapped key should be shoe horned to all new keys";
        assertEquals(msg, "ORIGINAL_VALUE_D", System.getProperty("NEW_KEY_D1"));
        assertEquals(msg, "ORIGINAL_VALUE_D", System.getProperty("NEW_KEY_D2"));
        assertEquals("MultiMapped key should not be shoe horned to original key", null, System.getProperty("ORIGINAL_KEY_D"));
    }

    private static Shoehorn createShoehornFixture(Map<String, String> mappings) {
        final Map<String, String> feet = ImmutableMap.of(
                "ORIGINAL_KEY_A", "ORIGINAL_VALUE_A",
                "ORIGINAL_KEY_B", "ORIGINAL_VALUE_B",
                "ORIGINAL_KEY_C", "ORIGINAL_VALUE_C",
                "ORIGINAL_KEY_D", "ORIGINAL_VALUE_D"
        );

        return new Shoehorn(feet, mappings);
    }
}
