package io.rently.listingservice.services;

import io.rently.listingservice.models.ResponseContent;
import io.rently.listingservice.utils.Broadcaster;
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

    public static String saveImage(String id, Object data) {
        String requestUrl = null;
        try {
            requestUrl = BASE_URL + "api/v1/images/" + id;
            restTemplate.postForObject(requestUrl, data, ResponseContent.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not save image url from image service: " + exception.getMessage());
        }
        return requestUrl;
    }

    public static String updateImage(String id, Object data) {
        String requestUrl = null;
        try {
            requestUrl = BASE_URL + "api/v1/images/" + id;
            restTemplate.put(requestUrl, data, ResponseContent.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not get new image url from image service: " + exception.getMessage());
        }
        return requestUrl;
    }
}
