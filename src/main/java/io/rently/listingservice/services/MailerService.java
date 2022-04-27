package io.rently.listingservice.services;

import io.rently.listingservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {
    public static String BASE_URL;
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${mailer.baseurl}")
    public void setBaseUrl(String baseUrl) {
        MailerService.BASE_URL = baseUrl;
    }

    public static void dispatchNewListing(String recipientEmail, String listingTitle, String listingLink, String listingDescription, String listingImage) {
        Broadcaster.info("Sending new listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "NEW_LISTING");
        data.put("title", listingTitle);
        data.put("link", listingLink);
        data.put("description", listingDescription);
        data.put("image", listingImage);
        data.put("email", recipientEmail);
        try {
            restTemplate.postForObject(BASE_URL + "api/v1/emails/dispatch/", data, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send new listing email to " + recipientEmail);
            Broadcaster.error(ex);
        }
    }

    public static void dispatchUpdatedListing(String recipientEmail, String listingTitle, String listingLink, String listingDescription, String listingImage) {
        Broadcaster.info("Sending updated listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "UPDATED_LISTING");
        data.put("title", listingTitle);
        data.put("link", listingLink);
        data.put("description", listingDescription);
        data.put("image", listingImage);
        data.put("email", recipientEmail);
        try {
            restTemplate.postForObject(BASE_URL + "api/v1/emails/dispatch/", data, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send updated listing email to " + recipientEmail);
            Broadcaster.error(ex);
        }
    }

    public static void dispatchDeletedListing(String recipientEmail, String listingTitle, String listingLink, String listingDescription, String listingImage) {
        Broadcaster.info("Sending deleted listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "LISTING_DELETION");
        data.put("email", recipientEmail);
        try {
            restTemplate.postForObject(BASE_URL + "api/v1/emails/dispatch/", data, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send deleted listing email to " + recipientEmail);
            Broadcaster.error(ex);
        }
    }

    public static void dispatchErrorToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Listing service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        try {
            restTemplate.postForObject(BASE_URL + "api/v1/emails/dispatch/", report, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report.");
            Broadcaster.error(ex);
        }
    }
}
