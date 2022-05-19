package io.rently.listingservice.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.exceptions.Errors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
public class Jwt {

    private final JwtParser parser;
    private final SignatureAlgorithm algo;
    private final SecretKeySpec secretKeySpec;

    public Jwt(String secret, SignatureAlgorithm algo) {
        if (!Objects.equals(algo.getFamilyName(), "HMAC")) {
            throw new IllegalArgumentException("Algorithm outside of `HMAC` family: " + algo.getFamilyName());
        }
        if (secret == null || secret.equals("")) {
            throw new IllegalArgumentException("Signing secret cannot be null or an empty string");
        }
        this.secretKeySpec = new SecretKeySpec(secret.getBytes(), algo.getJcaName());
        this.algo = algo;
        this.parser = Jwts.parser().setSigningKey(secretKeySpec);
    }

    public boolean validateBearerToken(String token) {
        try {
            String bearer = token.split(" ")[1];
            parser.parse(bearer);
        } catch (ExpiredJwtException exception) {
            throw Errors.EXPIRED_TOKEN;
        } catch (MalformedJwtException exception) {
            throw Errors.MALFORMED_TOKEN;
        } catch (Exception exception) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
        return true;
    }

    public String generateBearToken() {
        String id = UUID.randomUUID().toString();
        Date iat = new Date();
        Date ext = new Date(System.currentTimeMillis() + 60000L);

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(iat)
                .setExpiration(ext)
                .signWith(algo, secretKeySpec)
                .compact();
    }

    public Claims getClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }
}
