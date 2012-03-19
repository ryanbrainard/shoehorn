package shoehorn;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Ryan Brainard
 */
public class MappingLoaderTest {

    @Test
    public void testGetMappings_Null() {
        final Map<String, String> environment = ImmutableMap.of();

        final Map<String, String> mappings = new MappingLoader().load(environment);

        assertTrue(mappings.isEmpty());
    }

    @Test
    public void testGetMappings_Empty() {
        final Map<String, String> environment = ImmutableMap.of();

        final Map<String, String> mappings = new MappingLoader().load(environment);

        assertTrue(mappings.isEmpty());
    }

    @Test
    public void testGetMappings_FromEnvironment() {
        final Map<String, String> environment = ImmutableMap.of(
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX, "SOME_KEY_1=SOME_VALUE_1;SOME_KEY_2=SOME_VALUE_2");

        final Map<String, String> mappings = new MappingLoader().load(environment);

        assertMappings(mappings);
    }

    @Test
    public void testGetMappings_FromEnvironment_Multi() {
        final Map<String, String> environment = ImmutableMap.of(
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX + 1, "SOME_KEY_1=SOME_VALUE_1",
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX + 2, "SOME_KEY_2=SOME_VALUE_2");

        final Map<String, String> mappings = new MappingLoader().load(environment);

        assertMappings(mappings);
    }

    private void assertMappings(Map<String, String> mappings) {
        assertTrue(mappings.containsKey("SOME_KEY_1"));
        assertEquals("SOME_VALUE_1", mappings.get("SOME_KEY_1"));

        assertTrue(mappings.containsKey("SOME_KEY_2"));
        assertEquals("SOME_VALUE_2", mappings.get("SOME_KEY_2"));

        assertFalse(mappings.containsKey("MISSING_KEY"));
        assertEquals(null, mappings.get("MISSING_KEY"));
    }
}
