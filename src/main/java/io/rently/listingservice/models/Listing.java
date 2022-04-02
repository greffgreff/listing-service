package io.rently.listingservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("listings")
@JsonDeserialize(builder = Listing.Builder.class)
public class Listing {
    @Id
    public final String id;
    public final String name;
    public final String desc;
    public final String price;
    public final String image;
    public final String startDate;
    public final String endDate;
    public final String createdAt;
    public final String updatedAt;
    public final Address address;
    public final String leaser;
    public final String phone;

    public Listing(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.desc = builder.desc;
        this.price = builder.price;
        this.image = builder.image;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.address = builder.address;
        this.leaser = builder.leaser;
        this.phone = builder.phone;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", address=" + address +
                ", leaser='" + leaser + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static class Builder {
        @JsonProperty
        private final String id;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final String price;
        @JsonProperty
        private final String startDate;
        @JsonProperty
        private final String endDate;
        @JsonProperty
        private final String createdAt;
        @JsonProperty
        public String updatedAt;
        @JsonProperty
        private String image;
        @JsonProperty
        private String desc;
        @JsonProperty
        private final String leaser;
        @JsonProperty
        private String phone;
        @JsonProperty
        private Address address;

        public Builder(String id, String name, String price, String startDate, String endDate, String createdAt, String leaser) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.startDate = startDate;
            this.endDate = endDate;
            this.createdAt = createdAt;
            this.leaser = leaser;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setDesc(String desc) {
            this.desc = desc;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        @JsonCreator
        public Listing build() {
            return new Listing(this);
        }
    }
}
