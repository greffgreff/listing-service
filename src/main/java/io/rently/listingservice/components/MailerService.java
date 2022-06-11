package io.rently.listingservice.components;

import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailerService {

    private final RestTemplate restTemplate;
    private final Jwt jwt;
    private final String endPointUrl;

    public MailerService(Jwt jwt, String baseUrl, RestTemplate restTemplate) {
        this.jwt = jwt;
        this.endPointUrl = baseUrl + "api/v1/emails/dispatch/";
        this.restTemplate = restTemplate;
    }

    public synchronized void dispatchNewListingNotification(String recipientEmail, String listingTitle, String listingLink, String listingDescription, String listingImage) {
        Broadcaster.info("Sending new listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "NEW_LISTING");
        data.put("title", listingTitle);
        data.put("link", listingLink);
        data.put("description", listingDescription);
        data.put("image", listingImage);
        data.put("email", recipientEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForObject(endPointUrl, body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send new listing email to " + recipientEmail + ": " + ex.getMessage());
        }
    }

    public synchronized void dispatchUpdatedListingNotification(String recipientEmail, String listingTitle, String listingLink, String listingDescription, String listingImage) {
        Broadcaster.info("Sending updated listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "UPDATED_LISTING");
        data.put("title", listingTitle);
        data.put("link", listingLink);
        data.put("description", listingDescription);
        data.put("image", listingImage);
        data.put("email", recipientEmail);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForObject(endPointUrl, body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send updated listing email to " + recipientEmail + ": " + ex.getMessage());
        }
    }

    public synchronized void dispatchDeletedListingNotification(String recipientEmail, String listingTitle, String listingDescription) {
        Broadcaster.info("Sending deleted listing email to " + recipientEmail);
        Map<String, String> data = new HashMap<>();
        data.put("type", "LISTING_DELETION");
        data.put("email", recipientEmail);
        data.put("title", listingTitle);
        data.put("description", listingDescription);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, String>> body = new HttpEntity<>(data, headers);
        try {
            restTemplate.postForObject(endPointUrl, body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not send deleted listing email to " + recipientEmail + ": " + ex.getMessage());
        }
    }

    public synchronized void dispatchErrorReportToDevs(Exception exception) {
        Broadcaster.info("Dispatching error report...");
        Map<String, Object> report = new HashMap<>();
        report.put("type", "DEV_ERROR");
        report.put("datetime", new Date());
        report.put("message", exception.getMessage());
        report.put("service", "Listing service");
        report.put("cause", exception.getCause());
        report.put("trace", Arrays.toString(exception.getStackTrace()));
        report.put("exceptionType", exception.getClass());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.generateBearToken());
        HttpEntity<Map<String, Object>> body = new HttpEntity<>(report, headers);
        try {
            restTemplate.postForObject(endPointUrl, body, String.class);
        } catch (Exception ex) {
            Broadcaster.warn("Could not dispatch error report: " + ex.getMessage());
        }
    }
}
