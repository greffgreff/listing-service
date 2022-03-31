package io.rently.listingservice.mongodb;

import io.rently.listingservice.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ListingsRepository extends MongoRepository<Listing, String> {
}
