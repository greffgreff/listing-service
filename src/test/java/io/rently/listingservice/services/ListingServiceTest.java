package io.rently.listingservice.services;

import io.rently.listingservice.configs.BugsnagTestConfigs;
import io.rently.listingservice.configs.ListingServiceTestConfigs;
import io.rently.listingservice.dtos.Listing;
import io.rently.listingservice.exceptions.Errors;
import io.rently.listingservice.interfaces.ListingsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@WebMvcTest(ListingService.class)
@ContextConfiguration(classes = { ListingServiceTestConfigs.class, BugsnagTestConfigs.class })
class ListingServiceTest {

    @Autowired
    public ListingService service;
    @Autowired
    public ListingsRepository repository;

    Listing getValidGenericListing() {
        Listing.Address address = new Listing.Address();
        address.setFormattedAddress("Street number, Street name, City, Zip, Country");
        address.setCity("City");
        address.setCountry("Country");
        address.setStreet("Street");
        address.setZip("Zip");
        address.setLocation(new GeoJsonPoint(0.0, 0.0));
        Listing validListingData = new Listing();
        validListingData.setId(UUID.randomUUID().toString());
        validListingData.setImage("imageURL");
        validListingData.setLeaser(UUID.randomUUID().toString());
        validListingData.setName("My listing");
        validListingData.setDesc("A description");
        validListingData.setPhone("+1 07 12 23 34 45");
        validListingData.setPrice("123");
        validListingData.setStartDate(String.valueOf(new Date().getTime()));
        validListingData.setEndDate(String.valueOf(new Date().getTime()));
        validListingData.setUpdatedAt(String.valueOf(new Date().getTime()));
        validListingData.setCreatedAt(String.valueOf(new Date().getTime()));
        validListingData.setAddress(address);
        return validListingData;
    }

    @Test
    void getListingByProvider_validId_returnListing() {
        String id = UUID.randomUUID().toString();
        Listing expectedListing = new Listing();
        expectedListing.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(expectedListing));

        Listing userFound = service.getListingById(id);

        assert userFound == expectedListing;
    }

    @Test
    void getListingById_invalidListingId_throwNotFound() {
        String id = "invalid";

        when(repository.findById(id)).thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.LISTING_NOT_FOUND.getClass(), () -> service.getListingById(id));
    }

    @Test
    void addListing_existingListingId_throwAlreadyExists() {
        Listing existingListing = getValidGenericListing();

        when(repository.findById(existingListing.getId())).thenReturn(Optional.of(existingListing));

        assertThrows(Errors.LISTING_ALREADY_EXISTS.getClass(), () -> service.addListing(existingListing));
    }

    @Test
    void addListing_validListingData_void() {
        Listing validListingData = getValidGenericListing();

        assertDoesNotThrow(() -> service.addListing(validListingData));
    }

    @Test
    void updateListing_invalidListingId_throwNotFound() {
        String invalidDataHolderId = "invalid";
        Listing listing = getValidGenericListing();

        when(repository.findById(invalidDataHolderId)).thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.LISTING_NOT_FOUND.getClass(), () -> service.updateListing(invalidDataHolderId, listing));
    }

    @Test
    void updateListing_validListingData_void() {
        Listing validListingData = getValidGenericListing();
        String validId = validListingData.getId();

        when(repository.findById(validId)).thenReturn(Optional.of(validListingData));

        assertDoesNotThrow(() -> service.updateListing(validListingData.getId(), validListingData));
    }

    @Test
    void deleteListing_invalidId_throwNotFound() {
        String invalidDataHolderId = "invalid";

        when(repository.findById(invalidDataHolderId)).thenReturn(Optional.ofNullable(null));

        assertThrows(Errors.LISTING_NOT_FOUND.getClass(), () -> service.deleteListing(invalidDataHolderId));
    }

    @Test
    void deleteListing_validId_void() {
        Listing validListingData = getValidGenericListing();
        String validId = validListingData.getId();

        when(repository.findById(validId)).thenReturn(Optional.of(validListingData));

        assertDoesNotThrow(() -> service.deleteListing(validId));
    }
}