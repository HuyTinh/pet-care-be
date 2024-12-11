package com.pet_care.customer_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO representing a request to create a pet.
 * This class is used to encapsulate the information needed
 * for creating a new pet record.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetCreateRequest {

    /**
     * The unique identifier of the pet.
     */
    Long id;

    /**
     * The name of the pet.
     */
    String name;

    /**
     * The age of the pet, usually expressed as years.
     */
    String age;

    /**
     * The weight of the pet, expressed in kilograms or pounds.
     */
    Double weight;

    /**
     * The species of the pet (e.g., Dog, Cat).
     */
    String species;
}
