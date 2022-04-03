package io.rently.listingservice.controllers;

import io.rently.listingservice.models.Listing;
import io.rently.listingservice.models.ResponseContent;
import io.rently.listingservice.services.ListingService;
import io.rently.listingservice.utils.Broadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;

@RestController
public class ListingController {
    public static final String PREFIX = "/api/v1";

    @Autowired
    public ListingService service;

    @GetMapping(PREFIX + "/{id}")
    public ResponseContent handleGetRequest(@PathVariable String id) {
        Listing listing = service.getListingById(id);
        return new ResponseContent.Builder().setData(listing).build();
    }

    @PostMapping(PREFIX + "/")
    public ResponseContent handlePostRequest(@RequestBody Listing listing) {
        service.addListing(listing);
        return new ResponseContent.Builder().setMessage("Successfully added listing to database.").build();
    }
}
