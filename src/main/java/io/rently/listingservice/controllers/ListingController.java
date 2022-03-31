package io.rently.listingservice.controllers;

import io.rently.listingservice.models.Listing;
import io.rently.listingservice.models.ResponseContent;
import io.rently.listingservice.services.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ListingController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public ListingService service;

    @GetMapping("/**")
    public RedirectView redirectGets() {
        return new RedirectView(PREFIX + "/");
    }

    @PostMapping("/**")
    public RedirectView redirectPosts() {
        return new RedirectView(PREFIX + "/");
    }

    @GetMapping(PREFIX + "/")
    public ResponseContent handleGetRequest() {
        return new ResponseContent.Builder().setMessage("hello world").build();
    }

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest(@RequestBody Listing listing) {
        service.addListing(listing);
        return new ResponseContent.Builder().setData(listing).build();
    }
}
