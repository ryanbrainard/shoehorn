package shoehorn;

import java.util.Map;

/**
 * @author Ryan Brainard
 */
public class Shoehorn {

    public static void premain(String agentArgs) {
        System.out.println("Registering Environment Variables as Java System Properties");
        
        for (Map.Entry<String, String> envVar : System.getenv().entrySet()) {
            System.setProperty(envVar.getKey(), envVar.getValue());
        }
    }
}
