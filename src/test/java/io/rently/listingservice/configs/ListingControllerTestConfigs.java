package io.rently.listingservice.configs;

import io.rently.listingservice.interfaces.ListingsRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ListingControllerTestConfigs {

    @Bean
    @Primary
    public ListingsRepository userRepository() {
        return Mockito.mock(ListingsRepository.class);
    }
}
