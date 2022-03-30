package io.rently.listingservice.middlewares;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.utils.Broadcaster;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Interceptor implements HandlerInterceptor {
    private static final String secretKey = "HelloDarknessMyOldFriend"; // move to .env file
    public final List<String> blackListedMethods;

    public Interceptor(RequestMethod... excludedMethods) {
        this.blackListedMethods = Arrays.stream(excludedMethods).toList().stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        if (blackListedMethods.size() != 0) {
            Broadcaster.info("Loaded middleware with " + String.join(", ", blackListedMethods) + " method(s) disabled");
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (blackListedMethods.contains(request.getMethod())) return true;
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
