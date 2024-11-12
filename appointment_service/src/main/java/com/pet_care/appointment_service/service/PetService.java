package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.dto.response.PetResponse;
import com.pet_care.appointment_service.exception.APIException;
import com.pet_care.appointment_service.exception.ErrorCode;
import com.pet_care.appointment_service.mapper.PetMapper;
import com.pet_care.appointment_service.repository.PetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service // Marks this class as a service component for Spring to manage
@RequiredArgsConstructor // Lombok annotation to automatically generate a constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields to private and final
public class PetService {
    PetRepository petRepository; // Repository for interacting with the Pet data model
    PetMapper petMapper; // Mapper for converting Pet entities to PetResponse DTOs

    /**
     * Retrieves all pets from the repository and maps them to PetResponse DTOs.
     * @return A list of PetResponse DTOs.
     */
    @Transactional(readOnly = true) // Marks this method as read-only for the transaction to optimize performance
    public List<PetResponse> getAllPet() {
        // Fetch all pets from the repository, map them to PetResponse DTOs, and return the list
        return petRepository.findAll().stream()
                .map(petMapper::toDto) // Convert each pet entity to PetResponse DTO
                .collect(toList()); // Collect results into a list
    }

    /**
     * Retrieves a pet by its ID and returns its PetResponse DTO.
     * Throws an exception if the pet is not found.
     * @param petId The ID of the pet to retrieve.
     * @return The PetResponse DTO of the pet.
     * @throws APIException if the pet with the given ID is not found.
     */
    @Transactional(readOnly = true) // Marks this method as read-only for the transaction to optimize performance
    public PetResponse getPetById(Long petId) {
        // Try to find the pet by its ID and map it to a PetResponse DTO, or throw an exception if not found
        return petRepository.findById(petId)
                .map(petMapper::toDto) // If found, convert the pet entity to PetResponse DTO
                .orElseThrow(() -> new APIException(ErrorCode.PET_NOT_FOUND)); // If not found, throw an exception with an error code
    }
}
