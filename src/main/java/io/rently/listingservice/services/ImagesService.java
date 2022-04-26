package io.rently.listingservice.services;

import io.rently.listingservice.models.ResponseContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImagesService {
    public static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();;

    @Value("${images.baseurl}")
    public void setBaseUrl(String baseUrl) {
        ImagesService.BASE_URL = baseUrl;
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
