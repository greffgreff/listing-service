package io.rently.listingservice.controllers;

import io.rently.listingservice.dtos.ResponseContent;
import io.rently.listingservice.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping(PREFIX + "/authenticate")
    public ResponseContent handleGetRequestAuth() {
        return new ResponseContent.Builder().setData("logged in").build();
    }
}
