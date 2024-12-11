package com.pet_care.customer_service.dto.response;

// Import necessary annotations for JSON handling and Lombok usage
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * Response object representing the customer data.
 * This class is used to structure the response for customer details.
 * It includes basic customer information such as name, email, phone number, and birth date.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {

    /**
     * Unique identifier for the customer.
     */
    Long id;

    /**
     * First name of the customer.
     */
    @JsonProperty("first_name")
    String firstName;

    /**
     * Last name of the customer.
     */
    @JsonProperty("last_name")
    String lastName;

    /**
     * URL of the customer's image.
     */
    @JsonProperty("image_url")
    String imageUrl;

    /**
     * Phone number of the customer.
     */
    @JsonProperty("phone_number")
    String phoneNumber;

    /**
     * Email address of the customer.
     */
    String email;

    /**
     * Physical address of the customer.
     */
    String address;

    /**
     * Gender of the customer (e.g., Male, Female).
     */
    @Enumerated(EnumType.STRING)
    Gender gender;

    /**
     * Birth date of the customer in "yyyy-MM-dd" format.
     */
    @JsonProperty("birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthDate;

    /**
     * Account ID associated with the customer.
     */
    @JsonProperty("account_id")
    Long accountId;
}
