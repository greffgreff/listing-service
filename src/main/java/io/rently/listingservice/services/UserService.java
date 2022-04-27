package io.rently.listingservice.services;

import io.rently.listingservice.utils.Broadcaster;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserService {
    public static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${users.baseurl}")
    public void setBaseUrl(String baseUrl) {
        UserService.BASE_URL = baseUrl;
    }

    public static String fetchUserEmailById(String id) {
        String requestUrl = BASE_URL + "api/v2/users/" + id;
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
