package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SoftEmployeeUpdateRequest {
    // First name of the employee
    @JsonProperty("first_name")  // Maps the "first_name" JSON field to the "firstName" Java field
            String firstName;

    // Last name of the employee
    @JsonProperty("last_name")  // Maps the "last_name" JSON field to the "lastName" Java field
            String lastName;

    // Employee's gender, represented as an Enum (e.g., Male, Female)
    @Enumerated(EnumType.STRING)  // Maps the Enum to its String representation in the database
            Gender gender;

    // Employee's phone number
    @JsonProperty("phone_number")  // Maps the "phone_number" JSON field to the "phoneNumber" Java field
            String phoneNumber;

    @JsonProperty("image_url")
    String imageUrl;
}
