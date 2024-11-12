package com.pet_care.employee_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object (DTO) for employee response.
 * This class is used to structure the data that will be returned as part of an employee response.
 * It contains details of an employee, such as their ID, name, gender, role, and contact information.
 */
@Getter  // Lombok annotation to automatically generate getter methods for all fields
@Setter  // Lombok annotation to automatically generate setter methods for all fields
@AllArgsConstructor  // Lombok annotation to generate a constructor with all fields
@NoArgsConstructor   // Lombok annotation to generate a no-argument constructor
@Builder  // Lombok annotation to implement the builder pattern for object creation
@FieldDefaults(level = AccessLevel.PRIVATE)  // Lombok annotation to set all fields to private by default
@JsonInclude(JsonInclude.Include.NON_NULL)  // Jackson annotation to exclude null values from the JSON response
public class EmployeeResponse {

    // Unique identifier for the employee
    Long id;

    // First name of the employee
    @JsonProperty("first_name")  // Custom JSON property name for the field
            String firstName;

    // Last name of the employee
    @JsonProperty("last_name")  // Custom JSON property name for the field
            String lastName;

    // URL for the employee's profile image
    @JsonProperty("image_url")  // Custom JSON property name for the field
            String imageUrl;

    // Employee's email address
    String email;

    // Employee's address
    String address;

    // Employee's gender, represented using an enumerated type
    @Enumerated(EnumType.STRING)  // Persist gender as a string value in the database
            Gender gender;

    // Employee's role, represented using an enumerated type
    @Enumerated(EnumType.STRING)  // Persist role as a string value in the database
            Role role;

    // Account ID associated with the employee
    @JsonProperty("account_id")  // Custom JSON property name for the field
            Long accountId;

    // Employee's phone number
    @JsonProperty("phone_number")  // Custom JSON property name for the field
            String phoneNumber;
}
