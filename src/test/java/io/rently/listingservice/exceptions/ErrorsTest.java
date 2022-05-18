package io.rently.listingservice.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

class ErrorsTest {

    @Test
    void HttpFieldMissing() {
        Errors.HttpFieldMissing httpFieldMissing = new Errors.HttpFieldMissing("id");

        assert httpFieldMissing.getStatus() == HttpStatus.NOT_ACCEPTABLE;
    }

    @Test
    void HttpValidationFailure() {
        Errors.HttpValidationFailure httpFieldMissing = new Errors.HttpValidationFailure("id", UUID.class, "abc");

        assert httpFieldMissing.getStatus() == HttpStatus.NOT_ACCEPTABLE;
    }
}