package io.rently.listingservice.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rently.listingservice.exceptions.HttpValidationFailure;
import io.rently.listingservice.utils.Utils;
import io.rently.listingservice.utils.Validation;

import java.sql.Timestamp;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Contact.Builder.class)
public class Contact {
    public final String id;
    public final String name;
    public final String email;
    public final String phone;

    public Contact(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    public static class Builder {
        @JsonProperty
        private final String id;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final String email;
        @JsonProperty
        private String phone;

        public Builder(String id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Builder setEmail(String email) {
            this.phone = phone;
            return this;
        }

        @JsonCreator
        public Contact build() {
            validateFields();
            return new Contact(this);
        }

        private void validateFields() {
            if (Validation.tryParseUUID(id) == null) {
                throw new HttpValidationFailure("id", UUID.class, id);
            }
        }
    }
}
