package com.pet_care.appointment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

/**
 * Response object for customer details.
 * It contains personal information about a customer and their associated pets.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields from the JSON response
public class CustomerResponse {

    // Customer's unique ID
    Long id;

    // Customer's account ID
    @JsonProperty("account_id")
    Long accountId;

    // Customer's first name
    @JsonProperty("first_name")
    String firstName;

    // Customer's last name
    @JsonProperty("last_name")
    String lastName;

    // Customer's phone number
    @JsonProperty("phone_number")
    String phoneNumber;

    // Customer's address
    String address;

    // Customer's email
    String email;

    // Customer's gender
    String gender;

    // URL for the customer's profile image
    @JsonProperty("image_url")
    String imageUrl;

    // Set of pets associated with the customer
    Set<PetResponse> pets;
}
