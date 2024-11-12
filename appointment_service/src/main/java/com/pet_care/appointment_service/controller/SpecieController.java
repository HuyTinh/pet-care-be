package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.model.Specie;
import com.pet_care.appointment_service.service.SpecieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SpecieController handles HTTP requests related to species.
 */
@RestController
@RequestMapping("specie")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecieController {

    // The SpecieService is used to handle the business logic related to species.
    SpecieService specieService;

    /**
     * Retrieves a list of all species.
     *
     * @return A response containing the list of all species.
     */
    @GetMapping
    public APIResponse<List<Specie>> getAllSpecie() {
        // Calls the service layer to get all species and returns the response.
        return APIResponse.<List<Specie>>builder().data(specieService.getAll()).build();
    }

}
