package io.rently.listingservice.configs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.listingservice.components.UserService;
import io.rently.listingservice.middlewares.Interceptor;
import io.rently.listingservice.components.ImagesService;
import io.rently.listingservice.components.MailerService;
import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfigs implements WebMvcConfigurer {

    @Value("${server.secret}")
    public String secret;
    @Value("${server.algo}")
    public SignatureAlgorithm algo;
    public RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor(new Jwt(this.secret, this.algo), RequestMethod.GET));
    }

    @Bean
    public Jwt jwt() {
        Broadcaster.info("Loaded service middleware with secret `" + this.secret + "`");
        Broadcaster.info("Loaded service middleware with algo `" + this.algo + "`");
        return new Jwt(this.secret, this.algo);
    }

    @Bean
    public MailerService mailerService(
            @Value("${mailer.secret}") String secret,
            @Value("${mailer.algo}") SignatureAlgorithm algo,
            @Value("${mailer.baseurl}") String baseUrl
    ) {
        Broadcaster.info("Loaded MailerService with base URL `" + baseUrl + "`");
        Broadcaster.info("Loaded MailerService with secret `" + secret + "`");
        Broadcaster.info("Loaded MailerService with algo `" + algo + "`");
        return new MailerService(new Jwt(secret, algo), baseUrl, this.restTemplate);
    }

    @Bean
    public ImagesService imagesService(
            @Value("${images.secret}") String secret,
            @Value("${images.algo}") SignatureAlgorithm algo,
            @Value("${images.baseurl}") String baseUrl
    ) {
        Broadcaster.info("Loaded ImageService with base URL `" + baseUrl + "`");
        Broadcaster.info("Loaded ImageService with secret `" + secret + "`");
        Broadcaster.info("Loaded ImageService with algo `" + algo + "`");
        return new ImagesService(new Jwt(secret, algo), baseUrl, this.restTemplate);
    }

    @Bean
    public UserService userService(@Value("${users.baseurl}") String baseUrl) {
        Broadcaster.info("Loaded UserService with base URL `" + baseUrl + "`");
        return new UserService(baseUrl, this.restTemplate);
    }

}