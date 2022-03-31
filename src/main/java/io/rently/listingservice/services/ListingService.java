package io.rently.listingservice.services;

import io.rently.listingservice.models.Listing;
import io.rently.listingservice.mongodb.ListingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

@Service
@EnableMongoRepositories(basePackageClasses = ListingsRepository.class)
public class ListingService {

    @Autowired
    private ListingsRepository repository;

    public void addListing(Listing listing) {
        repository.insert(listing);
    }
}
