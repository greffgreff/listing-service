package io.rently.listingservice.services;

import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.models.Listing;
import io.rently.listingservice.mongodb.ListingsRepository;
import io.rently.listingservice.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@EnableMongoRepositories(basePackageClasses = ListingsRepository.class)
public class ListingService {

    @Autowired
    private ListingsRepository repository;

    public void addListing(Listing listing) {
        repository.insert(listing);
    }

    private static void validateData(Listing listing) {
        if (listing.id == null) {
            throw new IllegalArgumentException();
        } else if (Validation.tryParseUUID(listing.id) == null) {
            throw new Errors.HttpValidationFailure("id", UUID.class, listing.id);
        }
        if (listing.price == null) {
            throw new IllegalArgumentException();
        } else if (!Validation.canParseNumeric(listing.price)) {
            throw new Errors.HttpValidationFailure("price", Integer.class, listing.price);
        }
        if (listing.startDate == null) {
            throw new IllegalArgumentException();
        } else if (!Validation.canParseToTs(listing.startDate)) {
            throw new Errors.HttpValidationFailure("startDate", Timestamp.class, listing.startDate);
        }
        if (listing.endDate == null) {
            throw new IllegalArgumentException();
        } else if (!Validation.canParseToTs(listing.endDate)) {
            throw new Errors.HttpValidationFailure("endDate", Timestamp.class, listing.endDate);
        }
        if (listing.createdAt == null) {
            throw new IllegalArgumentException();
        } else if (!Validation.canParseToTs(listing.createdAt)) {
            throw new Errors.HttpValidationFailure("createdAt", Timestamp.class, listing.createdAt);
        }
        if (listing.leaser == null) {
            throw new IllegalArgumentException();
        } else if (Validation.tryParseUUID(listing.leaser) == null) {
            throw new Errors.HttpValidationFailure("createdAt", Timestamp.class, listing.createdAt);
        }
        if (listing.address.country == null) {
            throw new IllegalArgumentException();
        }
        if (listing.address.city == null) {
            throw new IllegalArgumentException();
        }
        if (listing.address.zip == null) {
            throw new IllegalArgumentException();
        }
    }
}
