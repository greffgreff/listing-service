package io.rently.listingservice.middlewares;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.utils.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpHeaders;
import java.util.Base64;

@Component
public class Interceptor implements HandlerInterceptor {
    private static final String secretKey = "HelloDarknessMyOldFriend"; // move to .env file

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String bearer = request.getHeader("Authorization");
        if (bearer == null) throw Errors.INVALID_REQUEST.getException();
        if (!validateBearerToken(bearer)) throw Errors.UNAUTHORIZED_REQUEST.getException();
        return true;
    }

    public static boolean validateBearerToken(String bearer) {
        String token = bearer.split(" ")[1];
        String[] chunks = token.split("\\.");
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        return validator.isValid(tokenWithoutSignature, signature);
    }

    // Base64.Decoder decoder = Base64.getUrlDecoder();
    // String header = new String(decoder.decode(chunks[0]));
    // String payload = new String(decoder.decode(chunks[1]));
}
