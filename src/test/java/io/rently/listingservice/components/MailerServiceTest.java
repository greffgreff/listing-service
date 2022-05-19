package io.rently.listingservice.components;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.listingservice.configs.BugsnagTestConfigs;
import io.rently.listingservice.dtos.Listing;
import io.rently.listingservice.utils.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@WebMvcTest(MailerService.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class MailerServiceTest {

    public MailerService mailerService;
    public static final String URL = "/";
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public RestTemplate restTemplate;

    Listing getValidGenericListing() {
        Listing.Address address = new Listing.Address();
        address.setFormattedAddress("Street number, Street name, City, Zip, Country");
        address.setCity("City");
        address.setCountry("Country");
        address.setStreet("Street");
        address.setZip("Zip");
        address.setLocation(new GeoJsonPoint(0.0, 0.0));
        Listing validListingData = new Listing();
        validListingData.setId(UUID.randomUUID().toString());
        validListingData.setImage("imageURL");
        validListingData.setLeaser(UUID.randomUUID().toString());
        validListingData.setName("My listing");
        validListingData.setDesc("A description");
        validListingData.setPhone("+1 07 12 23 34 45");
        validListingData.setPrice("123");
        validListingData.setStartDate(String.valueOf(new Date().getTime()));
        validListingData.setEndDate(String.valueOf(new Date().getTime()));
        validListingData.setUpdatedAt(String.valueOf(new Date().getTime()));
        validListingData.setCreatedAt(String.valueOf(new Date().getTime()));
        validListingData.setAddress(address);
        return validListingData;
    }

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        mailerService = new MailerService(new Jwt(SECRET, ALGORITHM), URL, restTemplate);

        getValidGenericListing();
    }

    @Test
    void dispatchNewListingNotification() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        assertDoesNotThrow(() -> mailerService.dispatchNewListingNotification(
                leaserEmail,
                listing.getName(),
                null,
                listing.getDesc(),
                listing.getImage()));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body -> {
                    assert body.toString().contains("type=NEW_LISTING");
                    assert body.toString().contains("title=" + listing.getName());
                    assert body.toString().contains("link=" + null);
                    assert body.toString().contains("description=" + listing.getDesc());
                    assert body.toString().contains("image=" + listing.getImage());
                    assert body.toString().contains("email=" + leaserEmail);
                    return true;
                }),
                Mockito.any()
        );
    }

    @Test
    void dispatchNewListingNotification_cannotConnectToService_void() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        when(restTemplate.postForObject( eq(URL + "api/v1/emails/dispatch/"), any(), anyString().getClass()))
                .thenThrow(new RestClientException("Failed to connection to mailer service..."));

        assertDoesNotThrow(() -> mailerService.dispatchNewListingNotification(
                leaserEmail,
                listing.getName(),
                null,
                listing.getDesc(),
                listing.getImage()));
    }

    @Test
    void dispatchUpdatedListingNotification() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        assertDoesNotThrow(() -> mailerService.dispatchUpdatedListingNotification(
                leaserEmail,
                listing.getName(),
                null,
                listing.getDesc(),
                listing.getImage()));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body -> {
                    assert body.toString().contains("type=UPDATED_LISTING");
                    assert body.toString().contains("title=" + listing.getName());
                    assert body.toString().contains("link=" + null);
                    assert body.toString().contains("description=" + listing.getDesc());
                    assert body.toString().contains("image=" + listing.getImage());
                    assert body.toString().contains("email=" + leaserEmail);
                    return true;
                }),
                Mockito.any()
        );
    }

    @Test
    void dispatchUpdatedListingNotification_cannotConnectToService_void() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        when(restTemplate.postForObject( eq(URL + "api/v1/emails/dispatch/"), any(), anyString().getClass()))
                .thenThrow(new RestClientException("Failed to connection to mailer service..."));

        assertDoesNotThrow(() -> mailerService.dispatchUpdatedListingNotification(
                leaserEmail,
                listing.getName(),
                null,
                listing.getDesc(),
                listing.getImage()));
    }

    @Test
    void dispatchDeletedListingNotification() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        assertDoesNotThrow(() -> mailerService.dispatchDeletedListingNotification(
                leaserEmail,
                listing.getName(),
                listing.getDesc()));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body -> {
                    assert body.toString().contains("type=LISTING_DELETION");
                    assert body.toString().contains("title=" + listing.getName());
                    assert body.toString().contains("description=" + listing.getDesc());
                    assert body.toString().contains("email=" + leaserEmail);
                    return true;
                }),
                Mockito.any()
        );
    }

    @Test
    void dispatchDeletedListingNotification_cannotConnectToService_void() {
        Listing listing = getValidGenericListing();
        String leaserEmail = "myEmailAddress";

        when(restTemplate.postForObject( eq(URL + "api/v1/emails/dispatch/"), any(), anyString().getClass()))
                .thenThrow(new RestClientException("Failed to connection to mailer service..."));

        assertDoesNotThrow(() -> mailerService.dispatchDeletedListingNotification(
                leaserEmail,
                listing.getName(),
                listing.getDesc()));
    }

    @Test
    void dispatchErrorToDevs_nonEmptyException_void() {
        Exception exception = new Exception("My exception", new Throwable("My cause"));

        assertDoesNotThrow(() -> mailerService.dispatchErrorReportToDevs(exception));

        verify(restTemplate, times(1)).postForObject(
                Mockito.eq(URL + "api/v1/emails/dispatch/"),
                argThat(body -> {
                    assert body.toString().contains("type=DEV_ERROR");
                    assert body.toString().contains("datetime");
                    assert body.toString().contains("message=" + exception.getMessage());
                    assert body.toString().contains("cause=" + exception.getCause());
                    assert body.toString().contains("trace=" + Arrays.toString(exception.getStackTrace()));
                    assert body.toString().contains("service=Listing service");
                    assert body.toString().contains("exceptionType=" + exception.getClass());
                    assert body.toString().contains("Authorization");
                    return true;
                }),
                Mockito.any()
        );
    }

    @Test
    void dispatchErrorToDevs_cannotConnectToService_void() {
        Exception exception = new Exception("My exception", new Throwable("My cause"));

        when(restTemplate.postForObject( eq(URL + "api/v1/emails/dispatch/"), any(), anyString().getClass()))
                .thenThrow(new RestClientException("Failed to connection to mailer service..."));

        assertDoesNotThrow(() -> mailerService.dispatchErrorReportToDevs(exception));
    }

    @Test
    void dispatchErrorToDevs_emptyException_nullExceptionThrown() {
        assertThrows(NullPointerException.class, () -> mailerService.dispatchErrorReportToDevs(null));
    }
}