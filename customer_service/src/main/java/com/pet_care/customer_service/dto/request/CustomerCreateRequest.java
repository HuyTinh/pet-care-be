package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * DTO representing a customer creation request.
 * This class is used to encapsulate the customer information needed
 * for creating a new customer.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreateRequest {

    /**
     * The email address of the customer.
     */
    String email;

    /**
     * The first name of the customer.
     * Serialized as "first_name" to match external API format.
     */
    @JsonProperty("first_name")
    String firstName;

    /**
     * The last name of the customer.
     * Serialized as "last_name" for consistent API interaction.
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
     * Uses the Gender enum to store gender as a string value.
     */
    @Enumerated(EnumType.STRING)
    Gender gender;

    /**
     * The address of the customer.
     */
    String address;

    /**
     * The image URL associated with the customer.
     * Serialized as "image_url" to match the expected API format.
     * If no image URL is provided, a default image is generated based on the customer's name.
     */
    @JsonProperty("image_url")
    String imageUrl;

    /**
     * The birth date of the customer.
     * Serialized as "birth_date" to match the expected date format in API.
     */
    @JsonProperty("birth_date")
    Date birthDate;

    /**
     * The account ID associated with the customer.
     * Serialized as "account_id" to match external API format.
     */
    @JsonProperty("account_id")
    Long accountId;

    /**
     * Custom getter for imageUrl.
     * If no image URL is provided, generate a default image URL
     * based on the customer's first and last name.
     * @return the image URL
     */
    public String getImageUrl() {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            return "https://api.multiavatar.com/" + this.firstName + this.lastName + ".png";
        }
        return imageUrl;
    }
}
