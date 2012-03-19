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
    public void testShoehorn_MappedKey() throws Exception {
        createShoehornFixture(ImmutableMap.of("sys.prop.b", "ORIGINAL_KEY_B")).shoehorn();

        assertEquals("Mapped key should be shoe horned to new key", "ORIGINAL_VALUE_B", System.getProperty("sys.prop.b"));
        assertEquals("Mapped key should not be shoe horned to original key", null, System.getProperty("ORIGINAL_KEY_B"));
    }

    private static Shoehorn createShoehornFixture(Map<String, String> mappings) {
        final Map<String, String> feet = ImmutableMap.of(
                "ORIGINAL_KEY_A", "ORIGINAL_VALUE_A",
                "ORIGINAL_KEY_B", "ORIGINAL_VALUE_B"
        );

        return new Shoehorn(feet, mappings);
    }
}
