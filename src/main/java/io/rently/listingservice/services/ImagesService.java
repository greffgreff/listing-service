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
        try {
            Broadcaster.debug(BASE_URL + "api/v1/images/" + id);
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            return restTemplate.postForObject(requestUrl, data, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not save image url from image service: " + exception.getMessage());
        }
        return null;
    }

    public static void updateImage(String id, Object data) {
        try {
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            restTemplate.put(requestUrl, data, String.class);
        } catch (Exception exception) {
            Broadcaster.warn("Could not get new image url from image service: " + exception.getMessage());
        }
    }

    public static void deleteImage(String id) {
        try {
            String requestUrl = BASE_URL + "api/v1/images/" + id;
            restTemplate.delete(requestUrl);
        } catch (Exception exception) {
            Broadcaster.warn("Could not delete image url from image service: " + exception.getMessage());
        }
    }
}
