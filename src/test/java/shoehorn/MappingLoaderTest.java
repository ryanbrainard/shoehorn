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
    public void testGetMappings_Empty() {
        final Map<String, String> environment = ImmutableMap.of();
        final String overrides = "";

        final Map<String, String> mappings = new MappingLoader().load(environment, overrides);

        assertTrue(mappings.isEmpty());
    }

    @Test
    public void testGetMappings_FromEnvironment() {
        final Map<String, String> environment = ImmutableMap.of(
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX, "SOME_KEY=SOME_VALUE;BARE_KEY;BARE_KEY_WITH_EQUALS=");
        final String overrides = "";

        final Map<String, String> mappings = new MappingLoader().load(environment, overrides);

        assertMappings(mappings);
    }

    @Test
    public void testGetMappings_FromEnvironment_Multi() {
        final Map<String, String> environment = ImmutableMap.of(
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX + 1, "SOME_KEY=SOME_VALUE;BARE_KEY_WITH_EQUALS=",
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX + 2, "BARE_KEY");
        final String overrides = "";

        final Map<String, String> mappings = new MappingLoader().load(environment, overrides);

        assertMappings(mappings);
    }

    @Test
    public void testGetMappings_FromEnvironment_Overridden() {
        final Map<String, String> environment = ImmutableMap.of(
                MappingLoader.SHOEHORN_MAP_ENV_VAR_PREFIX, "SOME_KEY=SOME_VALUE_XXX;BARE_KEY;BARE_KEY_WITH_EQUALS=");
        final String overrides = "SOME_KEY=SOME_VALUE";

        final Map<String, String> mappings = new MappingLoader().load(environment, overrides);

        assertMappings(mappings);
    }

    @Test
    public void testGetMappings_FromArg() {
        final Map<String, String> environment = ImmutableMap.of();
        final String overrides = "SOME_KEY=SOME_VALUE;BARE_KEY;BARE_KEY_WITH_EQUALS=";

        final Map<String, String> mappings = new MappingLoader().load(environment, overrides);

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
