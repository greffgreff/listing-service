package io.rently.listingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Errors {
    UNAUTHORIZED_REQUEST(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer is either no longer invalid or has been tampered with.")),
    INVALID_REQUEST(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No bearer found in the request.")),
    DATABASE_CONNECTION_FAILED(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to establish connection to database.")),
    INVALID_URI_PATH(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Invalid or incomplete URI.")),
    INTERNAL_SERVER_ERROR(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred. Request could not be completed.")),
    NO_DATA(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content found in request body.")),
    INVALID_DATA(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid data provided by client."));

    private final ResponseStatusException exception;

    Errors(ResponseStatusException exception) {
        this.exception = exception;
    }

    public ResponseStatusException getException() {
        return exception;
    }
}
