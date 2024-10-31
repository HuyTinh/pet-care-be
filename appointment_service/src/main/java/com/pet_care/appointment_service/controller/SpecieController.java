package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.model.Specie;
import com.pet_care.appointment_service.service.SpecieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("specie")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecieController {

     SpecieService specieService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<Specie>> getAllSpecie() {
        return APIResponse.<List<Specie>>builder().data(specieService.getAll()).build();
    }

}
