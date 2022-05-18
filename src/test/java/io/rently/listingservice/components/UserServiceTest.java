package io.rently.listingservice.components;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    public UserService service;
    public static final String URL = "/api/v2/users";
    public RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        service = new UserService(URL, restTemplate);
    }

    @Test
    void fetchUserEmailById_validUserId_returnEmail() throws JSONException {
        String id = UUID.randomUUID().toString();
        String expectedEmail = "myemail@gmail.com";
        String returnBody = new JSONObject().put("content", new JSONObject().put("email", expectedEmail)).toString();

        when(restTemplate.getForObject(URL +  "api/v2/users/"  + id, String.class)).thenReturn(returnBody);

        String email = service.fetchUserEmailById(id);

        assert Objects.equals(email, expectedEmail);
    }

    @Test
    void fetchUserEmailById_invalidUserId_returnNull() {
        String id = UUID.randomUUID().toString();

        String email = service.fetchUserEmailById(id);

        assert email == null;
    }
}