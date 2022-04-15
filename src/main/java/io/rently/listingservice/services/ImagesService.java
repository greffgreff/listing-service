package io.rently.listingservice.services;

import io.rently.listingservice.models.ResponseContent;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImagesService {
    public static final String BASE_URL = "http://localhost:8082/api/v1/images/";
    private final RestTemplate restTemplate;

    public ImagesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ImagesService() {
        this(new RestTemplate());
    }

    public String getImageDataUrl(String id) {
        String requestUrl = BASE_URL + id;
        return restTemplate.getForObject(requestUrl, String.class);
    }

    public void saveImage(String id, Object data) {
        String requestUrl = BASE_URL + id;
        restTemplate.postForObject(requestUrl, data, ResponseContent.class);
    }

    public void updateImage(String id, Object data) {
        String requestUrl = BASE_URL + id;
        restTemplate.put(requestUrl, data, ResponseContent.class);
    }
}
