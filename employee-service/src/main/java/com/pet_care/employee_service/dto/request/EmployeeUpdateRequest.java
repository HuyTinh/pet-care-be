package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object (DTO) class used for updating existing employee records.
 * This class is used to receive updated employee data from the client side (e.g., API requests).
 */
@Getter  // Generates getter methods for all fields
@Setter  // Generates setter methods for all fields
@Builder  // Generates a builder pattern for object creation
@AllArgsConstructor  // Generates a constructor with all fields
@NoArgsConstructor  // Generates a default no-argument constructor
@FieldDefaults(level = AccessLevel.PRIVATE)  // All fields are private by default
@ToString  // Generates the toString method for easy debugging and logging
public class EmployeeUpdateRequest {

    // First name of the employee
    @JsonProperty("first_name")  // Maps the "first_name" JSON field to the "firstName" Java field
            String firstName;

    // Last name of the employee
    @JsonProperty("last_name")  // Maps the "last_name" JSON field to the "lastName" Java field
            String lastName;

    // URL for the employee's image
    @JsonProperty("image_url")  // Maps the "image_url" JSON field to the "imageUrl" Java field
            String imageUrl;

    // Employee's email address
    String email;

    // Employee's physical address
    String address;

    // Employee's gender, represented as an Enum (e.g., Male, Female)
    @Enumerated(EnumType.STRING)  // Maps the Enum to its String representation in the database
            Gender gender;

    // Role assigned to the employee (e.g., Admin, Manager, User)
    @Enumerated(EnumType.STRING)  // Maps the Enum to its String representation in the database
            Role role;

    // The account ID associated with the employee
    @JsonProperty("account_id")  // Maps the "account_id" JSON field to the "accountId" Java field
            Long accountId;

    // Employee's phone number
    @JsonProperty("phone_number")  // Maps the "phone_number" JSON field to the "phoneNumber" Java field
            String phoneNumber;
}
