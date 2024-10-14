package com.pet_care.medicine_service.controller;

import com.pet_care.medicine_service.dto.response.APIResponse;
import com.pet_care.medicine_service.dto.response.LocationResponse;
import com.pet_care.medicine_service.service.LocationService;
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
@RequestMapping("location")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationController {
    LocationService locationService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<LocationResponse>> getAllLocation() {
        return APIResponse.<List<LocationResponse>>builder()
                .data(locationService.getAllLocations())
                .build();
    }

    /**
     * @param locationId
     * @return
     */
    @GetMapping("/{locationId}")
    public APIResponse<LocationResponse> getLocationById(@PathVariable("locationId") Long locationId) {
        return APIResponse.<LocationResponse>builder()
                .data(locationService.getLocationById(locationId))
                .build();
    }
}
