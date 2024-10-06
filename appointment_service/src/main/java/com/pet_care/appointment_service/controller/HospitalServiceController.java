package com.pet_care.appointment_service.controller;

import com.pet_care.appointment_service.dto.request.HospitalServiceRequest;
import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.HospitalServiceResponse;
import com.pet_care.appointment_service.service.HospitalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hospital-service")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalServiceController {
    HospitalService hospitalService;

    @PostMapping
    APIResponse<HospitalServiceResponse> createHospitalService(@RequestBody HospitalServiceRequest hospitalServiceRequest) {
        return APIResponse.<HospitalServiceResponse>builder()
                .result(hospitalService.createHospitalService(hospitalServiceRequest))
                .build();
    }

    @GetMapping
    APIResponse<List<HospitalServiceResponse>> getAllHospitalService() {
        return APIResponse.<List<HospitalServiceResponse>>builder()
                .result(hospitalService.getAllHospitalService()).build();
    }

    @GetMapping("{service}")
    APIResponse<HospitalServiceResponse> getHospitalServiceById(@PathVariable("service") String service) {
        return APIResponse.<HospitalServiceResponse>builder().result(hospitalService.getHospitalServiceById(service)).build();
    }
}
