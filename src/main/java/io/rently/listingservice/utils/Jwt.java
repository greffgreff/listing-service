package io.rently.listingservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.exceptions.Errors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Component
public class Jwt {
    private static DefaultJwtSignatureValidator validator;
    private static JwtParser parser;
    private static final SignatureAlgorithm algo = SignatureAlgorithm.HS256;
    public static SecretKeySpec secretKeySpec;

    @Value("${server.secret}")
    public void setSecret(String secret) {
        secretKeySpec = new SecretKeySpec(secret.getBytes(), algo.getJcaName());
        Jwt.validator = new DefaultJwtSignatureValidator(algo, secretKeySpec);
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

    public static String generateBearerToken() {
        String id = UUID.randomUUID().toString();
        Date iat = new Date();
        Date ext =  new Date(System.currentTimeMillis() + 5000L);

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(iat)
                .setExpiration(ext)
                .signWith(algo, secretKeySpec)
                .compact();
    }
}
