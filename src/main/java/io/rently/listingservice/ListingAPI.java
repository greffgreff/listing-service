package io.rently.listingservice;

import io.rently.listingservice.mongodb.ListingsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class ListingAPI {

	public static void main(String[] args) {
		SpringApplication.run(ListingAPI.class, args);
	}

}
