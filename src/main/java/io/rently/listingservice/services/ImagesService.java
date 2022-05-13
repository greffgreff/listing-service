package io.rently.listingservice.services;

import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImagesService {

    private final RestTemplate restTemplate;
    private final Jwt jwt;
    private final String endPointUrl;

    public ImagesService(Jwt jwt, String baseUrl, RestTemplate restTemplate) {
        this.jwt = jwt;
        this.endPointUrl = baseUrl + "api/v1/images/";
        this.restTemplate = restTemplate;
    }

    public String saveImage(String id, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Object> body = new HttpEntity<>(data, headers);
        try {
            String requestUrl = endPointUrl + id;
            return restTemplate.postForObject(requestUrl, body, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not save image url from image service: " + exception.getMessage());
        }
        return null;
    }

    public String updateImage(String id, Object data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Object> body = new HttpEntity<>(data, headers);
        try {
            String requestUrl = endPointUrl + id;
            return restTemplate.exchange(requestUrl, HttpMethod.PUT, body, String.class).getBody();
        } catch (Exception exception) {
            Broadcaster.warn("Could not get new image url from image service: " + exception.getMessage());
        }
        return null;
    }

    public void deleteImage(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<String> body = new HttpEntity<>(null, headers);
        try {
            String requestUrl = endPointUrl + id;
            restTemplate.exchange(requestUrl, HttpMethod.DELETE, body, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not delete image url from image service: " + exception.getMessage());
        }
    }
}
