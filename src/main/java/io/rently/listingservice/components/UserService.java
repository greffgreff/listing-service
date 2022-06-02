package io.rently.listingservice.components;

import io.rently.listingservice.utils.Broadcaster;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserService {

    private final RestTemplate restTemplate;
    private final String endPointUrl;

    public UserService(String endPointUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.endPointUrl = endPointUrl;
    }

    public String fetchUserEmailById(String id) {
        String requestUrl = endPointUrl + "api/v2/users/" + id;
        String email = null;
        try {
            String userData = restTemplate.getForObject(requestUrl, String.class);
            JSONObject json = new JSONObject(userData);
            email = json.getJSONObject("content").getString("email");
        } catch (Exception exception) {
            Broadcaster.warn("Could not get user email by user id: " + exception.getMessage());
        }
        return email;
    }
}
