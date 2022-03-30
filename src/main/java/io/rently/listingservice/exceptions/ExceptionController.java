package io.rently.listingservice.exceptions;

import io.rently.listingservice.dtos.ResponseContent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseContent handleGenericException(HttpServletResponse response, Exception exception) {
        ResponseStatusException resEx = Errors.INTERNAL_SERVER_ERROR.getException();
        response.setStatus(resEx.getStatus().value());
        return new ResponseContent.Builder(resEx.getStatus()).setMessage(resEx.getMessage()).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public static ResponseContent handleResponseException(HttpServletResponse response, ResponseStatusException ex) {
        response.setStatus(ex.getStatus().value());
        return new ResponseContent.Builder(ex.getStatus()).setMessage(ex.getReason()).build();
    }
}
