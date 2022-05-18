package io.rently.listingservice.configs;

import com.bugsnag.Bugsnag;
import io.rently.listingservice.components.MailerService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ExceptionControllerTestConfigs {

    @Bean
    @Primary
    public MailerService mailerService() {
        return Mockito.mock(MailerService.class);
    }

    @Bean
    @Primary
    public Bugsnag bugsnag() {
        return Mockito.mock(Bugsnag.class);
    }
}
