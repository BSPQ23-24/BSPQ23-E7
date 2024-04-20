package es.deusto.spq.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class ClientManager {
    
    private static ClientManager instance;
    private Client client;
    private WebTarget webTarget;
    

    private ClientManager() {
        
        client = ClientBuilder.newClient();
    }

    public static ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    public WebTarget getWebTarget() {
        return webTarget;
    }
    
    public void setWebTarget(String hostname, String port)
    {
        webTarget = client.target(String.format("http://%s:%s/deustocars", hostname, port));
    }
}