package io.rently.listingservice.interfaces;

import io.rently.listingservice.dtos.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ListingsRepository extends MongoRepository<Listing, String> {

}
