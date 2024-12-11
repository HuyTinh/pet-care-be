package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.PetResponse;
import com.pet_care.appointment_service.service.PetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PetController handles HTTP requests related to pets, such as retrieving pet details.
 */
@RestController
@RequestMapping("pet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetController {

    // The PetService is used to handle the business logic related to pets.
    PetService petService;

    /**
     * Retrieves a pet's details based on the pet ID.
     *
     * @param petId The ID of the pet to retrieve.
     * @return A response containing the pet details.
     */
    @GetMapping("/{petId}")
    public APIResponse<PetResponse> getPetById(@PathVariable("petId") Long petId) {
        // Calls the service layer to get a pet by its ID and returns the response.
        return APIResponse.<PetResponse>builder()
                .data(petService.getPetById(petId))
                .build();
    }
}
