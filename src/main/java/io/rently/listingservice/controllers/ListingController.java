package io.rently.listingservice.controllers;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.rently.listingservice.dtos.ResponseContent;
import io.rently.listingservice.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
public class ListingController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public ListingService service;

    @GetMapping("/**")
    public RedirectView redirect() {
        return new RedirectView(PREFIX + "/");
    }

    @GetMapping(PREFIX + "/")
    public ResponseContent handleGetRequest() {
        return new ResponseContent.Builder().setMessage("hello world").build();
    }

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest(@RequestHeader("Authorization") String bearer) {
        String token = bearer.split(" ")[1];

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        String secretKey = "HelloDarknessMyOldFriend";
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        String tokenWithoutSignature = chunks[0] + "." + chunks[1];
        String signature = chunks[2];

        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);

        System.out.println(validator.isValid(tokenWithoutSignature, signature));

        return new ResponseContent.Builder().setMessage("hello world").build();
    }
}
