package io.rently.listingservice.errors;

import com.bugsnag.Bugsnag;
import io.rently.listingservice.components.MailerService;
import io.rently.listingservice.configs.ExceptionControllerTestConfigs;
import io.rently.listingservice.dtos.ResponseContent;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.exceptions.ExceptionController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(ExceptionController.class)
@ContextConfiguration(classes = ExceptionControllerTestConfigs.class)
class ExceptionControllerTest {

    @Autowired
    public ExceptionController controller;
    public MockHttpServletResponse response;
    @Autowired
    public MailerService mailer;
    @Autowired
    public Bugsnag bugsnag;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
    }

    @Test
    void unhandledException_mailerInvoked_bugsnagInvoked() {
        Exception exception = new Exception("This is an unhandled exception");
        ResponseStatusException expectedException = Errors.INTERNAL_SERVER_ERROR;

        ResponseContent content = controller.unhandledException(response, exception);

        verify(mailer, times(1)).dispatchErrorReportToDevs(
                argThat(ex -> {
                    assert Objects.equals(ex.getMessage(), exception.getMessage());
                    return true;
                })
        );

        verify(bugsnag, times(1)).notify(
                (Throwable) argThat(thrw -> {
                    assert thrw.getClass() == exception.getClass();
                    return true;
                })
        );

        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }

    @Test
    void responseException() {
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "This is a bad request exception");

        ResponseContent content = ExceptionController.handleResponseException(response, exception);

        assert response.getStatus() == exception.getStatus().value();
        assert content.getStatus() == exception.getStatus().value();
        assert Objects.requireNonNull(exception.getMessage()).contains(content.getMessage());
    }

    @Test
    void invalidURI() {
        ResponseStatusException expectedException = Errors.INVALID_URI_PATH;

        ResponseContent content = ExceptionController.handleInvalidURI(response);

        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }

    @Test
    void invalidFormatException() {
        ResponseStatusException expectedException = Errors.NO_DATA;

        ResponseContent content = ExceptionController.handleInvalidFormatException(response);

        assert response.getStatus() == expectedException.getStatus().value();
        assert content.getStatus() == expectedException.getStatus().value();
        assert Objects.requireNonNull(expectedException.getMessage()).contains(content.getMessage());
    }
}