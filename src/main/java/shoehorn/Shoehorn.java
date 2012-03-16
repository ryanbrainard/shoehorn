package shoehorn;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    private final Map<String, String> feet;
    private final Map<String, String> mappings;

    public Shoehorn(Map<String, String> feet, Map<String, String> mappings) {
        this.feet = feet;
        this.mappings = mappings;
    }

    void shoehorn() {
        for (Map.Entry<String, String> foot : feet.entrySet()) {
            if (!mappings.containsKey(foot.getKey())) {
                continue;
            }

            final String[] shoeKeys;
            if (mappings.get(foot.getKey()).trim().equals("")) {
                shoeKeys = new String[]{foot.getKey()};
            } else {
                shoeKeys = mappings.get(foot.getKey()).split(" ");
            }

            for (String shoeKey : shoeKeys) {
                System.setProperty(shoeKey, foot.getValue());
                System.out.println("Shoehorned [" + foot.getKey() + "] into [" + shoeKey + "]");
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void premain(String agentArgs) {
        new Shoehorn(System.getenv(), new MappingLoader().load(System.getenv())).shoehorn();
    }
}
