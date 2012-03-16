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
        for (Map.Entry<String, String> env : environment.entrySet()) {
            if (!mappings.containsKey(env.getKey())) {
                continue;
            }

            final String[] propertyKeys;
            if (mappings.get(env.getKey()).trim().equals("")) {
                propertyKeys = new String[]{env.getKey()};
            } else {
                propertyKeys = mappings.get(env.getKey()).split(" ");
            }

            for (String propertyKey : propertyKeys) {
                System.setProperty(propertyKey, env.getValue());
                System.out.println("Shoehorned [" + env.getKey() + "] into [" + propertyKey + "]");
            }
        }
    }
}
