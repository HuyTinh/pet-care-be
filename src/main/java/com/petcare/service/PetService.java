package com.petcare.service;

import com.petcare.dto.response.PetDetailResponse;
import com.petcare.dto.response.PetResponse;
import com.petcare.entity.Pet;
import com.petcare.mapper.PetDetailMapper;
import com.petcare.mapper.PetMapper;
import com.petcare.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<PetResponse> getAllPets() {

        List<Pet> pets = petRepository.findAll();

        return PetMapper.INSTANCE.mapperPetsToPetsResponse(pets);
    }

    public PetDetailResponse getPetById(long petId) {

        Pet pet = petRepository.findById(petId).get();

        return PetDetailMapper.INSTANCE.mapperPetToPetDetailResponse(pet);
//        return pet;
    }

}
