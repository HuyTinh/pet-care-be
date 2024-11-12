package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.model.Specie;
import com.pet_care.appointment_service.repository.SpecieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Marks this class as a service component to be managed by Spring
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields as private and final
public class SpecieService {
    SpecieRepository specieRepository; // Repository for interacting with Specie data model

    /**
     * Retrieves all species from the repository.
     * @return A list of all species.
     */
    @Transactional(readOnly = true) // Marks this method as read-only for transaction optimization
    public List<Specie> getAll() {
        // Fetch all species from the repository and return them as a list
        return specieRepository.findAll();
    }

    /**
     * Retrieves a species by its name.
     * Throws an exception if the species with the given name is not found.
     * @param name The name of the species to retrieve.
     * @return The Specie object corresponding to the name.
     * @throws APIException if the species is not found.
     */
    @Transactional(readOnly = true) // Marks this method as read-only for transaction optimization
    public Specie getByName(String name) {
        // Try to find the species by its name, or throw an exception if not found
        return specieRepository.findById(name)
                .orElseThrow(() -> new APIException(ErrorCode.SPECIE_NOT_FOUND)); // If not found, throw an exception with an error code
    }
}
