package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Data Transfer Object (DTO) class used for creating new employee records.
 * This class is used to receive employee data from the client side (e.g., API requests).
 */
@Getter  // Generates getter methods for all fields
@Setter  // Generates setter methods for all fields
@Builder  // Generates a builder pattern for object creation
@AllArgsConstructor  // Generates a constructor with all fields
@NoArgsConstructor  // Generates a default no-argument constructor
@FieldDefaults(level = AccessLevel.PRIVATE)  // All fields are private by default
@ToString  // Generates the toString method for easy debugging and logging
public class EmployeeCreateRequest {

    // First name of the employee
    @JsonProperty("first_name")  // Maps the "first_name" JSON field to the "firstName" Java field
            String firstName;

    // Last name of the employee
    @JsonProperty("last_name")  // Maps the "last_name" JSON field to the "lastName" Java field
            String lastName;

    // URL for the employee's image
    @JsonProperty("image_url")  // Maps the "image_url" JSON field to the "imageUrl" Java field
            String imageUrl;

    // Password for the employee's account
    String password;

    // Employee's email address
    String email;

    // Employee's physical address
    String address;

    // Employee's gender, represented as an Enum (e.g., Male, Female)
    @Enumerated(EnumType.STRING)  // Maps the Enum to its String representation in the database
            Gender gender;

    // Employee's phone number
    @JsonProperty("phone_number")  // Maps the "phone_number" JSON field to the "phoneNumber" Java field
            String phoneNumber;

    // Set of roles assigned to the employee (e.g., Admin, Manager, User)
    @Enumerated(EnumType.STRING)  // Maps the Enum to its String representation in the database
            Set<Role> roles;

    /**
     * Returns the image URL. If no image URL is provided, it generates a default image URL
     * using the first and last name of the employee.
     *
     * @return Image URL for the employee
     */
    public String getImageUrl() {
        // If no image URL is provided, return a generated default image URL based on the employee's name
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            return "https://api.multiavatar.com/" + this.firstName + this.lastName + ".png";
        }
        // Otherwise, return the provided image URL
        return imageUrl;
    }
}
