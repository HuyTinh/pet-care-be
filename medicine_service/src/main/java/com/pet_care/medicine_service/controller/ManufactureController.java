package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.ManufactureResponse;
import com.pet_care.medicine_service.service.ManufactureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("manufacture")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufactureController {

    ManufactureService manufactureService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<ManufactureResponse>> getAllManufacture() {
        return APIResponse.<List<ManufactureResponse>>builder()
                .data(manufactureService.getAllManufacture())
                .build();
    }

    /**
     * @param manufactureId
     * @return
     */
    @GetMapping("/{manufactureId}")
    public APIResponse<ManufactureResponse> getManufactureById(@PathVariable("manufactureId") Long manufactureId) {
        return APIResponse.<ManufactureResponse>builder()
                .data(manufactureService.getManufactureById(manufactureId))
                .build();
    }

}
