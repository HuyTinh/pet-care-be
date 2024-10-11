package com.pet_care.search_service.service;

import com.pet_care.search_service.model.Pet;
import com.pet_care.search_service.repository.PetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetService {
    @NotNull PetRepository petRepository;

    @NotNull
    public Pet insertPet(@NotNull Pet pet) {
        return petRepository.save(pet);
    }

    @NotNull
    public Iterable<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
