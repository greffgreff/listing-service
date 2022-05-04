package io.rently.listingservice.services;

import io.rently.listingservice.models.ResponseContent;
import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ImagesService {
    public static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();;

    @Value("${images.baseurl}")
    public void setBaseUrl(String baseUrl) {
        ImagesService.BASE_URL = baseUrl;
    }

    public static String saveImage(String id, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Jwt.generateBearerToken());
        HttpEntity<Object> body = new HttpEntity<>(data, headers);
        try {
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            return restTemplate.postForObject(requestUrl, body, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not save image url from image service: " + exception.getMessage());
        }
        return null;
    }

    public static String updateImage(String id, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Jwt.generateBearerToken());
        HttpEntity<Object> body = new HttpEntity<>(data, headers);
        try {
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            return restTemplate.exchange(requestUrl, HttpMethod.PUT, body, String.class).getBody();
        } catch (Exception exception) {
            Broadcaster.warn("Could not get new image url from image service: " + exception.getMessage());
        }
        return null;
    }

    public static void deleteImage(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Jwt.generateBearerToken());
        HttpEntity<String> body = new HttpEntity<>(null, headers);
        try {
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            restTemplate.exchange(requestUrl, HttpMethod.DELETE, body, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not delete image url from image service: " + exception.getMessage());
        }
    }
}
