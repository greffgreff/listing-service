package io.rently.listingservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import javax.crypto.spec.SecretKeySpec;

public class Jwt {
    private static final String SECRET = "HelloDarknessMyOldFriend"; // move to .env file
    private static final SignatureAlgorithm ALGO = SignatureAlgorithm.HS256; // move to .env file
    private static final SecretKeySpec SECRET_KEY_SPEC;
    public static final DefaultJwtSignatureValidator VALIDATOR;
    public static final JwtParser PARSER;

    public static boolean validateBearerToken(String token) {
        String bearer = token.split(" ")[1];
        String[] chunks = bearer.split("\\.");
        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];
        return VALIDATOR.isValid(tokenWithoutSignature, signature);
    }

    public static Claims getClaims(String token) {
        String bearer = token.split(" ")[1];
        return PARSER.parseClaimsJws(bearer).getBody();
    }

    static {
        SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(), ALGO.getJcaName());
        VALIDATOR = new DefaultJwtSignatureValidator(ALGO, SECRET_KEY_SPEC);
        PARSER = Jwts.parser().setSigningKey(SECRET_KEY_SPEC);
    }
}
