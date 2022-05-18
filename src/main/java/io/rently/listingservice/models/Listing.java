package io.rently.listingservice.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("listings")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Listing {
    @Id
    private String id;
    private String name;
    private String desc;
    private String price;
    private String image;
    private String startDate;
    private String endDate;
    private String createdAt;
    private String updatedAt;
    private Address address;
    private String leaser;
    private String phone;

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class Address {
        private String street;
        private String city;
        private String zip;
        private String country;
        private String formattedAddress;
        private GeoJsonPoint location;
    }
}
