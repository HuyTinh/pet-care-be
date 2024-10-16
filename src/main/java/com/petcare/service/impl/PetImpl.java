package com.petcare.service.impl;

import com.petcare.entity.Pet;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.PetRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetImpl implements EntityService<Pet, Long> {

    @Autowired
    private PetRepository petRepository;

    @Override
    public List<Pet> getAllEntity() {
        return ArrayMapper.mapperIterableToList(petRepository.findAll());
    }

    @Override
    public Optional<Pet> getEntityById(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public Pet createEntity(Pet pet) {
        Optional<Pet> petOptional = petRepository.findById(pet.getId());
        if (petOptional.isPresent()) {
            throw new APIException(ErrorCode.PET_ALREADY_EXISTS);
        }
        return petRepository.save(pet);
    }

    @Override
    public Pet updateEntity(Pet pet) {
        Pet existingPet = petRepository.findById(pet.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PET_NOT_FOUND));
        return petRepository.save(pet);
    }

    @Override
    public void deleteEntity(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.PET_NOT_FOUND));
        petRepository.delete(pet);
    }
}

