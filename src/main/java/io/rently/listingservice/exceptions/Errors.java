package io.rently.listingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {
    public static final ResponseStatusException  UNAUTHORIZED_REQUEST = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer is either no longer invalid or has been tampered with.");
    public static final ResponseStatusException INVALID_REQUEST = new ResponseStatusException(HttpStatus.BAD_REQUEST, "No bearer found in the request.");
    public static final ResponseStatusException DATABASE_CONNECTION_FAILED = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database.");
    public static final ResponseStatusException INVALID_URI_PATH = new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Invalid or incomplete URI.");
    public static final ResponseStatusException INTERNAL_SERVER_ERROR = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Request could not be completed.");
    public static final ResponseStatusException NO_DATA = new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content found in request body.");
    public static final ResponseStatusException INVALID_DATA = new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid data provided by client.");

    public static class HttpFieldMissing extends ResponseStatusException {
        public HttpFieldMissing(String fieldName) {
            super(HttpStatus.NOT_ACCEPTABLE, "A non-optional field has missing value. Value of field '" + fieldName + "' was expected but got null.");
        }
    }

    public static class HttpValidationFailure extends ResponseStatusException {
        public HttpValidationFailure(String fieldName, Class<?> fieldType, String value) {
            super(HttpStatus.NOT_ACCEPTABLE, "Validation failure occurred. Value of field '" + fieldName + "' could not be recognized as type " + fieldType.getName() + " (value: '" + value + "')");
        }
    }
}
