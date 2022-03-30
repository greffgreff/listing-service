package io.rently.listingservice.controllers;

import io.rently.listingservice.dtos.ResponseContent;
import io.rently.listingservice.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest() {
        return new ResponseContent.Builder().setMessage("hello world").build();
    }
}
