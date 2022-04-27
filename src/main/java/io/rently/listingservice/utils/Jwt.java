package io.rently.listingservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.exceptions.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class Jwt {
    private static DefaultJwtSignatureValidator validator;
    private static JwtParser parser;

    @Value("${server.secret}")
    public void setSecret(String secret) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        Jwt.validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpec);
        Jwt.parser = Jwts.parser().setSigningKey(secretKeySpec);
    }

    public static boolean validateBearerToken(String token) {
        checkExpiration(token);
        String bearer = token.split(" ")[1];
        String[] chunks = bearer.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        return validator.isValid(tokenWithoutSignature, signature);
    }

    public static void checkExpiration(String token) {
        try {
            getClaims(token);
        } catch (Exception e) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }

    public static Claims getClaims(String token) {
        String bearer = token.split(" ")[1];
        return parser.parseClaimsJws(bearer).getBody();
    }
}
