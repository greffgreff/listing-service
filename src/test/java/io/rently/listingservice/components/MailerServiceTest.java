package io.rently.listingservice.components;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.listingservice.configs.BugsnagTestConfigs;
import io.rently.listingservice.utils.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(MailerService.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class MailerServiceTest {

    public MailerService mailerService;
    public static final String URL = "/";
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public static RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        mailerService = new MailerService(new Jwt(SECRET, ALGORITHM), URL, restTemplate);
    }

    @Test
    void dispatchNewListingNotification() {
    }

    @Test
    void dispatchUpdatedListingNotification() {
    }

    @Test
    void dispatchDeletedListingNotification() {
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
    void dispatchErrorToDevs_emptyException_nullExceptionThrown() {
        assertThrows(NullPointerException.class, () -> mailerService.dispatchErrorReportToDevs(null));
    }
}