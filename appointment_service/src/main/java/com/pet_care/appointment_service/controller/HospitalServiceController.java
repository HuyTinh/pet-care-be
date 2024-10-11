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

@RestController
@RequestMapping("hospital-service")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalServiceController {
    @NotNull HospitalService hospitalService;

    @PostMapping
    APIResponse<HospitalServiceResponse> createHospitalService(@RequestBody HospitalServiceRequest hospitalServiceRequest) {
        return APIResponse.<HospitalServiceResponse>builder()
                .data(hospitalService.createHospitalService(hospitalServiceRequest))
                .build();
    }

    @GetMapping
    APIResponse<List<HospitalServiceResponse>> getAllHospitalService() {
        return APIResponse.<List<HospitalServiceResponse>>builder()
                .data(hospitalService.getAllHospitalService()).build();
    }

    @GetMapping("{service}")
    APIResponse<HospitalServiceResponse> getHospitalServiceById(@NotNull @PathVariable("service") String service) {
        return APIResponse.<HospitalServiceResponse>builder().data(hospitalService.getHospitalServiceById(service)).build();
    }
}
