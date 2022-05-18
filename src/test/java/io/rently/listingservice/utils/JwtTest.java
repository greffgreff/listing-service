package io.rently.listingservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.listingservice.configs.BugsnagTestConfigs;
import io.rently.listingservice.exceptions.Errors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(Jwt.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class JwtTest {

    public Jwt jwt;
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(), ALGORITHM.getJcaName());

    @BeforeEach
    void setup() {
        this.jwt = new Jwt(SECRET, ALGORITHM);
    }

    @Test
    void invalidAlgo_agrException() {
        SignatureAlgorithm jwtAlgo = SignatureAlgorithm.ES256;

        assertThrows(IllegalArgumentException.class, () -> new Jwt(SECRET, jwtAlgo));
    }

    @Test
    void invalidSecret_agrException() {
        String jwtSecret = "";

        assertThrows(IllegalArgumentException.class, () -> new Jwt(jwtSecret, ALGORITHM));
    }

    @Test
    void generateBearerToken() {
        String token = jwt.generateBearToken();

        assert token.matches("(^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$)");
    }

    @Test
    void validateBearerToken_malformedToken_malformedException() {
        String token = "malformed token";

        assertThrows(Errors.MALFORMED_TOKEN.getClass(), () -> jwt.validateBearerToken(token));
    }

    @Test
    void validateBearerToken_invalidSecret_unauthorizedException() {
        Date validDate = new Date(System.currentTimeMillis() - 60000L);
        String invalidSecret = "invalid secret";
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(ALGORITHM, invalidSecret)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_invalidAlgo_unauthorizedException() {
        Date validDate = new Date(System.currentTimeMillis() - 60000L);
        SignatureAlgorithm invalidAlgo = SignatureAlgorithm.HS384;
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(invalidAlgo, SECRET_KEY_SPEC)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_expiredToken_tokenExpiredException() {
        Date pastDate = new Date(System.currentTimeMillis() - 60000L);
        String token = Jwts.builder()
                .setIssuedAt(pastDate)
                .setExpiration(pastDate)
                .signWith(ALGORITHM, SECRET_KEY_SPEC)
                .compact();
        String bearer = "Bearer " + token;

        assertThrows(Errors.EXPIRED_TOKEN.getClass(), () -> jwt.validateBearerToken(bearer));
    }

    @Test
    void validateBearerToken_validToken() {
        Date validDate = new Date(System.currentTimeMillis() + 60000L);
        String token = Jwts.builder()
                .setIssuedAt(validDate)
                .setExpiration(validDate)
                .signWith(ALGORITHM, SECRET_KEY_SPEC)
                .compact();
        String bearer = "Bearer " + token;

        assert jwt.validateBearerToken(bearer);
    }

    @Test
    void getParser() {
        assert jwt.getParser() != null;
    }
}