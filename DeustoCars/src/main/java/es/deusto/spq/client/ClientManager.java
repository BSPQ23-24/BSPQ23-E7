/**
 * This class represents a manager for the RESTful client.
 */
package es.deusto.spq.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * This class represents a manager for the RESTful client.
 */
public class ClientManager {
    
    private static ClientManager instance;
    private Client client;
    private WebTarget webTarget;
    

    /**
     * Constructs a new ClientManager object.
     */
    private ClientManager() {
        client = ClientBuilder.newClient();
    }

    /**
     * Retrieves the singleton instance of ClientManager.
     * @return The ClientManager instance.
     */
    public static ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    /**
     * Retrieves the web target.
     * @return The web target.
     */
    public WebTarget getWebTarget() {
        return webTarget;
    }
    
    /**
     * Sets the web target using the provided hostname and port.
     * @param hostname The hostname of the target.
     * @param port The port of the target.
     */
    public void setWebTarget(String hostname, String port) {
        webTarget = client.target(String.format("http://%s:%s/deustocars", hostname, port));
    }

}
