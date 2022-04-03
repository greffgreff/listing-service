package io.rently.listingservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Address.Builder.class)
public class Address {
    private String street;
    private String city;
    private String zip;
    private String country;

    protected Address() { }

    public Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.zip = builder.zip;
        this.country = builder.country;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public static class Builder {
        @JsonProperty
        private final String city;
        @JsonProperty
        private final String zip;
        @JsonProperty
        private final String country;
        @JsonProperty
        private String street;

        public Builder(String city, String zip, String country) {
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
            return new Address(this);
        }
    }
}
