package io.rently.listingservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.listingservice.exceptions.HttpValidationFailure;
import io.rently.listingservice.utils.Validation;

import java.sql.Timestamp;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Lease.Builder.class)
public class Lease {
    public final String id;
    public final String name;
    public final String desc;
    public final String price;
    public final String image;
    public final String startDate;
    public final String endDate;
    public final String createdAt;
    public final String address;
    public final Contact contact;

    public Lease(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.desc = builder.desc;
        this.price = builder.price;
        this.image = builder.image;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.createdAt = builder.createdAt;
        this.address = builder.address;
        this.contact = builder.contact;
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
        private String address;
        @JsonProperty
        private String image;
        @JsonProperty
        private String desc;
        @JsonProperty
        private final Contact contact;

        public Builder(String id, String name, String price, String startDate, String endDate, String createdAt, Contact contact) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.startDate = startDate;
            this.endDate = endDate;
            this.createdAt = createdAt;
            this.contact = contact;
        }

        public Builder setAddress(String address) {
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

        @JsonCreator
        public Lease build() {
            validateFields();
            return new Lease(this);
        }

        private void validateFields() {
            if (Validation.tryParseUUID(id) == null) {
                throw new HttpValidationFailure("id", UUID.class, id);
            }
            if (!Validation.canParseNumeric(price)) {
                throw new HttpValidationFailure("price", Integer.class, price);
            }
            if (!Validation.canParseToTs(startDate)) {
                throw new HttpValidationFailure("startDate", Timestamp.class, startDate);
            }
            if (!Validation.canParseToTs(endDate)) {
                throw new HttpValidationFailure("endDate", Timestamp.class, endDate);
            }
            if (!Validation.canParseToTs(createdAt)) {
                throw new HttpValidationFailure("createdAt", Timestamp.class, createdAt);
            }
        }
    }
}
