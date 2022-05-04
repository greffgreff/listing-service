package io.rently.listingservice.middlewares;

import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Interceptor implements HandlerInterceptor {
    private final List<String> blackListedMethods;

    public Interceptor(RequestMethod... excludedMethods) {
        this.blackListedMethods = Arrays.stream(excludedMethods).toList().stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());

        if (blackListedMethods.size() != 0) {
            Broadcaster.info("Loaded middleware with " + String.join(", ", blackListedMethods) + " method(s) disabled");
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            handleOptionRequest(response);
            return true;
        }
        if (blackListedMethods.contains(request.getMethod())) {
            return true;
        }

        String bearer = request.getHeader("Authorization");
        if (bearer == null) {
            throw Errors.INVALID_REQUEST;
        }

        if (!Jwt.validateBearerToken(bearer)) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }

        return true;
    }

    public void handleOptionRequest(HttpServletResponse response) {
        response.setHeader("Cache-Control","no-cache");
        response.setHeader("Access-control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setStatus(HttpStatus.OK.value());
    }
}
