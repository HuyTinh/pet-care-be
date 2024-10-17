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

@RestController
@RequestMapping("pet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetController {

    PetService petService;

    @GetMapping("/{petId}")
    public APIResponse<PetResponse> getPetById(@PathVariable("petId") Long petId) {
        return APIResponse.<PetResponse>builder()
                .data(petService.getPetById(petId))
                .build();
    }
}
