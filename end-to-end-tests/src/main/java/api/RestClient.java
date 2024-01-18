package api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestClient {
    private WebTarget baseUrl;

    public RestClient(String base) {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target(base);
    }

    public <T> T post(String subPath, Object obj, Class<T> responseType) throws Exception {
        WebTarget target = baseUrl.path(subPath);
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(obj));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new Exception("Error in registration ");
        }
        return response.readEntity(responseType);
    }

    public Response delete(String subPath) throws Exception {
        WebTarget target = baseUrl.path(subPath);
        Response response = target.request(MediaType.APPLICATION_JSON).delete();

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new Exception("Error: " + response.getStatus() + " - " + response.readEntity(String.class));
        }

        return response;
    }

    public <T> T get(String subPath, Class<T> responseType) throws Exception {

        WebTarget target = baseUrl.path(subPath);
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new Exception("Error: " + response.readEntity(String.class));
        }
        return response.readEntity(responseType);
    }
}
