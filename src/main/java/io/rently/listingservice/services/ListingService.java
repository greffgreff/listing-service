package io.rently.listingservice.services;

import io.rently.listingservice.components.ImagesService;
import io.rently.listingservice.components.MailerService;
import io.rently.listingservice.components.UserService;
import io.rently.listingservice.dtos.Listing;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.interfaces.ListingsRepository;
import io.rently.listingservice.utils.Broadcaster;
import io.rently.listingservice.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class ListingService {

    @Value("${rently.baseurl}")
    public String baseUrl;
    @Autowired
    private ListingsRepository repository;
    @Autowired
    public ImagesService imagesService;
    @Autowired
    private MailerService mailer;
    @Autowired
    private UserService userService;

    public Listing getListingById(String id) {
        Broadcaster.info("Fetching listing from database: " + id);
        return tryFindById(id);
    }

    public void addListing(Listing listing) {
        Broadcaster.info("Adding listing to database: " + listing.getId());
        validateData(listing);
        String imageUrl = imagesService.saveImage(listing.getId(), listing.getImage());
        listing.setImage(imageUrl);
        String userEmail = userService.fetchUserEmailById(listing.getLeaser());
        String listingUrl = baseUrl + "listings/" + listing.getId();
        mailer.dispatchNewListingNotification(userEmail, listing.getName(), listingUrl, listing.getDesc(), listing.getImage());
        Optional<Listing> existingListing = repository.findById(listing.getId());
        if (existingListing.isPresent()) {
            throw Errors.LISTING_ALREADY_EXISTS;
        }
        repository.save(listing);
    }

    public void updateListing(String id, Listing listing) {
        Broadcaster.info("Updating listing from database: " + id);
        validateData(listing);
        tryFindById(id);
        if (listing.getImage() != null && !listing.getImage().matches("(www|http:|https:)+[^\\s]+[\\w]")) {
            String imageUrl = imagesService.updateImage(listing.getId(), listing.getImage());
            listing.setImage(imageUrl);
        }
        String userEmail = userService.fetchUserEmailById(listing.getLeaser());
        String listingUrl = baseUrl + "listings/" + listing.getId();
        mailer.dispatchUpdatedListingNotification(userEmail, listing.getName(), listingUrl, listing.getDesc(), listing.getImage());
        repository.save(listing);
    }

    public void deleteListing(String id) {
        Broadcaster.info("Removing listing from database: " + id);
        Listing listing = tryFindById(id);
        imagesService.deleteImage(id);
        String userEmail = userService.fetchUserEmailById(listing.getLeaser());
        mailer.dispatchDeletedListingNotification(userEmail, listing.getName(), listing.getDesc());
        repository.deleteById(id);
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
        if (listing == null) {
            throw Errors.NO_DATA;
        } else if (listing.getId() == null) {
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
