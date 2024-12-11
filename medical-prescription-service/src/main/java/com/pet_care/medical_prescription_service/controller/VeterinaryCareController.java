package com.pet_care.medical_prescription_service.controller;

import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.VeterinaryCareResponse;
import com.pet_care.medical_prescription_service.entity.VeterinaryCare;
import com.pet_care.medical_prescription_service.service.VeterinaryCareService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("veterinary-care")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VeterinaryCareController
{
    VeterinaryCareService veterinaryCareService;

    @GetMapping
    public APIResponse<List<VeterinaryCareResponse>> getAllVeterinaryCares(){
        return APIResponse.<List<VeterinaryCareResponse>>builder()
                .data(veterinaryCareService.getAllVeterinaryCare())
                .build();
    }

    @GetMapping("{veterinaryCareId}")
    public APIResponse<VeterinaryCareResponse> getVeterinaryCareById(@PathVariable("veterinaryCareId") String veterinaryCareId){
        return APIResponse.<VeterinaryCareResponse>builder()
                .data(veterinaryCareService.getVeterinaryCareById(veterinaryCareId))
                .build();
    }
}
