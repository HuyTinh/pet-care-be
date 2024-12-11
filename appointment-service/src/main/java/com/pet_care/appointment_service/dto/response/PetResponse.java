package com.pet_care.appointment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * A response DTO for representing a pet's information.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetResponse {

    // The unique identifier for the pet
    Long id;

    // The name of the pet
    String name;

    // The age of the pet
    String age;

    // The weight of the pet
    Double weight;

    // The species of the pet
    String species;
}
