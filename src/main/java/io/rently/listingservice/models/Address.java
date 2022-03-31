package io.rently.listingservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.listingservice.exceptions.HttpValidationFailure;
import io.rently.listingservice.utils.Validation;

import java.util.UUID;

@JsonDeserialize(builder = Address.Builder.class)
public class Address {
    public String id;
    public String street;
    public String city;
    public String zip;
    public String country;

    public Address(Builder builder) {
        this.id = builder.id;
        this.street = builder.street;
        this.city = builder.city;
        this.zip = builder.zip;
        this.country = builder.country;
    }

    public static class Builder {
        @JsonProperty
        private final String id;
        @JsonProperty
        private final String city;
        @JsonProperty
        private final String zip;
        @JsonProperty
        private final String country;
        @JsonProperty
        private String street;

        public Builder(String id, String city, String zip, String country) {
            this.id = id;
            this.city = city;
            this.zip = zip;
            this.country = country;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        @JsonCreator
        public Address build() {
            validateFields();
            return new Address(this);
        }

        private void validateFields() {
            if (Validation.tryParseUUID(id) == null) {
                throw new HttpValidationFailure("id", UUID.class, id);
            }
        }
    }
}
