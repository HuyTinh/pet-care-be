package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.request.HospitalServiceRequest;
import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.HospitalServiceResponse;
import com.pet_care.appointment_service.service.HospitalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HospitalServiceController handles all HTTP requests related to hospital services,
 * including creating, retrieving, and managing hospital services.
 */
@RestController
@RequestMapping("hospital-service")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalServiceController {

    // The HospitalService is used to handle the business logic related to hospital services
    HospitalService hospitalService;

    /**
     * Creates a new hospital service based on the provided request data.
     *
     * @param hospitalServiceRequest The request body containing the details of the new hospital service.
     * @return A response containing the created hospital service details.
     */
    @PostMapping
    public APIResponse<HospitalServiceResponse> createHospitalService(@RequestBody HospitalServiceRequest hospitalServiceRequest) {
        // Calls the service layer to create a new hospital service and returns the response.
        return APIResponse.<HospitalServiceResponse>builder()
                .data(hospitalService.createHospitalService(hospitalServiceRequest))
                .build();
    }

    /**
     * Retrieves all hospital services from the system.
     *
     * @return A response containing the list of all hospital services.
     */
    @GetMapping
    public APIResponse<List<HospitalServiceResponse>> getAllHospitalService() {
        // Calls the service layer to get all hospital services and returns the response.
        return APIResponse.<List<HospitalServiceResponse>>builder()
                .data(hospitalService.getAllHospitalService())
                .build();
    }

    /**
     * Retrieves a hospital service by its ID (service name).
     *
     * @param service The ID (or name) of the hospital service to retrieve.
     * @return A response containing the details of the requested hospital service.
     */
    @GetMapping("{service}")
    public APIResponse<HospitalServiceResponse> getHospitalServiceById(@PathVariable("service") String service) {
        // Calls the service layer to get a hospital service by its ID and returns the response.
        return APIResponse.<HospitalServiceResponse>builder()
                .data(hospitalService.getHospitalServiceById(service))
                .build();
    }
}
