package io.rently.listingservice.controllers;

import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.models.Listing;
import io.rently.listingservice.models.ResponseContent;
import io.rently.listingservice.services.ListingService;
import io.rently.listingservice.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class ListingController {

    @Autowired
    public ListingService service;

    @GetMapping("/{id}")
    public ResponseContent handleGetRequest(@PathVariable String id) {
        Listing listing = service.getListingById(id);
        return new ResponseContent.Builder().setData(listing).build();
    }

    @PostMapping("/")
    public ResponseContent handlePostRequest(@RequestHeader("Authorization") String header, @RequestBody Listing listing) {
        verifyOwnership(header, listing.getId());
        service.addListing(listing);
        return new ResponseContent.Builder().setMessage("Successfully added listing to database.").build();
    }

    @DeleteMapping("/{id}")
    public ResponseContent handleDeleteRequest(@RequestHeader("Authorization") String header, @PathVariable String id) {
        verifyOwnership(header, id);
        service.deleteById(id);
        return new ResponseContent.Builder().setMessage("Successfully removed listing from database.").build();
    }

    @PutMapping("/{id}")
    public ResponseContent handlePutRequest(@RequestHeader("Authorization") String header, @PathVariable String id, @RequestBody Listing listing) {
        verifyOwnership(header, listing.getId());
        service.putById(id, listing);
        return new ResponseContent.Builder().setMessage("Successfully updated listing from database.").build();
    }

    public void verifyOwnership(String header, String listingId) {
        Listing listing = service.getListingById(listingId);
        String id = Jwt.getClaims(header).getSubject();
        if (!Objects.equals(id, listing.getLeaser())) {
            throw Errors.UNAUTHORIZED_REQUEST;
        }
    }
}
