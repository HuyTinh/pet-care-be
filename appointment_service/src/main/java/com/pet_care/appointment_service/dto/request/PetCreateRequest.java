package com.pet_care.appointment_service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for creating a new pet request. This class holds the necessary data to create
 * a new pet entry in the system.
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores any unknown properties during JSON deserialization
public class PetCreateRequest {

    // Name of the pet
    String name;

    // Age of the pet (as a string, possibly representing years or a specific format)
    String age;

    // Weight of the pet (in kilograms or pounds, depending on system)
    Double weight;

    // Species of the pet (e.g., Dog, Cat)
    String species;
}
