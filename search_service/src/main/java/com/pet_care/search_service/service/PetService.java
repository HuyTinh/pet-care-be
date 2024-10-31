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
     PetRepository petRepository;

    
    public Pet insertPet( Pet pet) {
        return petRepository.save(pet);
    }

    
    public Iterable<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
