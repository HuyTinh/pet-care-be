package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing a customer update request.
 * This class is used to encapsulate the customer information
 * needed for updating an existing customer's details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {

    /**
     * The first name of the customer.
     * Serialized as "first_name" to match external API format.
     */
    @JsonProperty("first_name")
    String firstName;

    /**
     * The last name of the customer.
     * Serialized as "last_name" to match external API format.
     */
    @JsonProperty("last_name")
    String lastName;

    /**
     * The phone number of the customer.
     * Serialized as "phone_number" to match external API format.
     */
    @JsonProperty("phone_number")
    String phoneNumber;

    /**
     * The gender of the customer.
     * Stored as an enum with the string representation for external API use.
     */
    @Enumerated(EnumType.STRING)
    Gender gender;

    /**
     * The image URL associated with the customer.
     * Serialized as "image_url" to match external API format.
     */
    @JsonProperty("image_url")
    String imageUrl;
}
