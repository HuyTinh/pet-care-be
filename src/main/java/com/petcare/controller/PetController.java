package com.petcare.controller;

import com.petcare.dto.response.PetDetailResponse;
import com.petcare.dto.response.PetResponse;
import com.petcare.entity.Pet;
import com.petcare.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/management")
@CrossOrigin("*")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping()
    public List<PetResponse> getPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{petId}")
    public PetDetailResponse getPetById(@PathVariable("petId") long petId) {
        return petService.getPetById(petId);
    }

}
