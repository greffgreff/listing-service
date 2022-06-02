package io.rently.listingservice.middlewares;

import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Interceptor implements HandlerInterceptor {

    private final Jwt jwt;
    public final List<String> blackListedMethods;

    public Interceptor(Jwt jwt, RequestMethod... excludedMethods) {
        this.jwt = jwt;
        this.blackListedMethods = Stream.of(excludedMethods)
                .map(m -> Objects.toString(m, null))
                .collect(Collectors.toList());

        if (blackListedMethods.size() != 0) {
            Broadcaster.info("Loaded middleware with " + String.join(", ", blackListedMethods) + " method(s) disabled");
        }
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setHeader("Cache-Control","no-cache");
            response.setHeader("Access-control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setStatus(HttpStatus.OK.value());
            return true;
        }
        if (blackListedMethods.contains(request.getMethod())) {
            return true;
        }
        String bearer = request.getHeader("Authorization");
        return jwt.validateBearerToken(bearer);
    }
}
