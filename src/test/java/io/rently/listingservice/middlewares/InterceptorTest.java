package io.rently.listingservice.middlewares;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.rently.listingservice.configs.BugsnagTestConfigs;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.utils.Jwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(Interceptor.class)
@ContextConfiguration(classes = BugsnagTestConfigs.class)
class InterceptorTest {

    public Interceptor interceptor;
    public MockHttpServletResponse response;
    public MockMultipartHttpServletRequest request;
    public static final String SECRET = "secret";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(), ALGORITHM.getJcaName());

    @BeforeEach
    void setup() {
        interceptor = new Interceptor(new Jwt(SECRET, ALGORITHM));
        request = new MockMultipartHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle_noJwt_unauthorizedRequestThrown() {
        assertThrows(Errors.UNAUTHORIZED_REQUEST.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_malFormedJwt_malformedRequestThrown() {
        request.addHeader("Authorization", "abc");

        assertThrows(Errors.MALFORMED_TOKEN.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_expiredJwt_expiredRequestThrown() {
        Date expiredDate = new Date(System.currentTimeMillis() - 60000L);
        String token = Jwts.builder()
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                .signWith(ALGORITHM, SECRET_KEY_SPEC)
                .compact();

        request.addHeader("Authorization", "Bearer " + token);

        assertThrows(Errors.EXPIRED_TOKEN.getClass(), () -> interceptor.preHandle(request, response, new Object()));
    }

    @Test
    void preHandle_validJwt_returnsTrue() {
        Date expiredDate = new Date(System.currentTimeMillis() + 60000L);
        String token = Jwts.builder()
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                .signWith(ALGORITHM, SECRET_KEY_SPEC)
                .compact();

        request.addHeader("Authorization", "Bearer " + token);

        assert interceptor.preHandle(request, response, new Object());
    }

    @Test
    void preHandle_whenRequestMethodIsOption_returnTrue() {
        request.setMethod(RequestMethod.OPTIONS.name());

        assert interceptor.preHandle(request, response, new Object());
        assert Objects.equals(response.getHeader("Cache-Control"), "no-cache");
        assert Objects.equals(response.getHeader("Access-control-Allow-Origin"), "*");
        assert Objects.equals(response.getHeader("Access-Control-Allow-Methods"), "GET,POST,OPTIONS,PUT,DELETE");
        assert Objects.equals(response.getHeader("Access-Control-Allow-Headers"), "*");
        assert response.getStatus() == HttpServletResponse.SC_OK;
    }

    @Test
    void preHandle_whenRequestMethodIsExcluded_returnTrue() {
        Interceptor interceptor = new Interceptor(new Jwt(SECRET, ALGORITHM), RequestMethod.GET);

        request.setMethod(RequestMethod.GET.name());

        assert interceptor.preHandle(request, response, new Object());
    }
}