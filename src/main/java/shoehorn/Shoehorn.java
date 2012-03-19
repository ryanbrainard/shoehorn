package shoehorn;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    @SuppressWarnings("UnusedDeclaration")
    public static void premain(String agentArgs) {
        new Shoehorn(System.getenv(), new MappingLoader().load(System.getenv())).shoehorn();
    }

    private final Map<String, String> environment;
    private final Map<String, String> mappings;

    public Shoehorn(Map<String, String> environment, Map<String, String> mappings) {
        this.environment = environment;
        this.mappings = mappings;
    }

    void shoehorn() {
        for (Map.Entry<String, String> map : mappings.entrySet()) {
            final String propName = map.getKey();
            final String envName = map.getValue();

            if (!environment.containsKey(envName)) {
                continue;
            }

            System.setProperty(propName, environment.get(envName));
            System.out.println("Shoehorned [" + envName + "] into [" + propName + "]");
        }
    }
}
