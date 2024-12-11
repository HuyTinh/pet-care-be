package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.service.ManufactureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("manufacture")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufactureController {

    ManufactureService manufactureService;

    /**
     * Retrieves all manufactures.
     *
     * @return A response containing the list of all manufactures.
     */
    @GetMapping
    public APIResponse<List<ManufactureResponse>> getAllManufacture() {
        return APIResponse.<List<ManufactureResponse>>builder()
                .data(manufactureService.getAllManufacture())
                .build();
    }

    /**
     * Retrieves a manufacture by its ID.
     *
     * @param manufactureId The ID of the manufacture to retrieve.
     * @return A response containing the manufacture details.
     */
    @GetMapping("/{manufactureId}")
    public APIResponse<ManufactureResponse> getManufactureById(@PathVariable("manufactureId") Long manufactureId) {
        return APIResponse.<ManufactureResponse>builder()
                .data(manufactureService.getManufactureById(manufactureId))
                .build();
    }
}
