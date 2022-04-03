package io.rently.listingservice.services;

import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.models.Listing;
import io.rently.listingservice.interfaces.ListingsRepository;
import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableMongoRepositories(basePackageClasses = ListingsRepository.class)
public class ListingService {

    @Autowired
    private ListingsRepository repository;

    public void addListing(Listing listing) {
        Broadcaster.info("Adding listing to database: " + listing.getId());
        validateData(listing);
        repository.insert(listing);
    }

    public Listing getListingById(String id) {
        Broadcaster.info("Fetching listing from database by id: " + id);
        return tryFindById(id);
    }

    public Listing tryFindById(String id) {
        Optional<Listing> listing = repository.findById(id);
        if (listing.isPresent()) {
            return listing.get();
        } else {
            throw Errors.LISTING_NOT_FOUND;
        }
    }

    private static void validateData(Listing listing) {
        if (listing.getId() == null) {
            throw new Errors.HttpFieldMissing("id");
        } else if (Validation.tryParseUUID(listing.getId()) == null) {
            throw new Errors.HttpValidationFailure("id", UUID.class, listing.getId());
        } else if (listing.getPrice() == null) {
            throw new Errors.HttpFieldMissing("price");
        } else if (!Validation.canParseNumeric(listing.getPrice())) {
            throw new Errors.HttpValidationFailure("price", Integer.class, listing.getPrice());
        } else if (listing.getStartDate() == null) {
            throw new Errors.HttpFieldMissing("startDate");
        } else if (!Validation.canParseToTs(listing.getStartDate())) {
            throw new Errors.HttpValidationFailure("startDate", Timestamp.class, listing.getStartDate());
        } else if (listing.getEndDate() == null) {
            throw new Errors.HttpFieldMissing("endDate");
        } else if (!Validation.canParseToTs(listing.getEndDate())) {
            throw new Errors.HttpValidationFailure("endDate", Timestamp.class, listing.getEndDate());
        } else if (listing.getCreatedAt() == null) {
            throw new Errors.HttpFieldMissing("createdAt");
        } else if (!Validation.canParseToTs(listing.getCreatedAt())) {
            throw new Errors.HttpValidationFailure("createdAt", Timestamp.class, listing.getCreatedAt());
        } else if (listing.getUpdatedAt() == null) {
            throw new Errors.HttpFieldMissing("updatedAt");
        } else if (!Validation.canParseToTs(listing.getUpdatedAt())) {
            throw new Errors.HttpValidationFailure("updatedAt", Timestamp.class, listing.getUpdatedAt());
        } else if (listing.getLeaser() == null) {
            throw new Errors.HttpFieldMissing("leaser");
        } else if (Validation.tryParseUUID(listing.getLeaser()) == null) {
            throw new Errors.HttpValidationFailure("leaser", Timestamp.class, listing.getLeaser());
        } else if (listing.getAddress().getCountry() == null) {
            throw new Errors.HttpFieldMissing("address.country");
        } else if (listing.getAddress().getCity() == null) {
            throw new Errors.HttpFieldMissing("address.city");
        } else if (listing.getAddress().getZip() == null) {
            throw new Errors.HttpFieldMissing("address.zip");
        }
    }
}
