package com.pet_care.medical_prescription_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.PageableResponse;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import com.pet_care.medical_prescription_service.service.PrescriptionService;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("prescription")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionController {

    PrescriptionService prescriptionService;

    PrescriptionRepository prescriptionRepository;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<PrescriptionResponse>> getAllPrescription() {
        return APIResponse.<List<PrescriptionResponse>>builder()
                .data(prescriptionService.getAllPrescriptions())
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<PageableResponse<PrescriptionResponse>> getFilteredPrescription(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "50") int size,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "statues", required = false, defaultValue = "APPROVED")  String prescriptionStatus
    ) throws JsonProcessingException {

        return APIResponse.<PageableResponse<PrescriptionResponse>>builder()
                .data(prescriptionService
                        .filteredPrescription(
                                page,
                                size,
                                Objects.requireNonNullElse(startDate, LocalDate.now()),
                                Objects.requireNonNullElse(endDate, LocalDate.now()),
                                PrescriptionStatus.valueOf(prescriptionStatus)
                        )
                )
                .build();
    }

    /**
     * @param prescriptionId
     * @return
     */
    @GetMapping("/{prescriptionId}")
    public APIResponse<PrescriptionResponse> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId) throws JsonProcessingException {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionById(prescriptionId))
                .build();
    }


    /**
     * @param appointmentId
     * @return
     */
    @GetMapping("/{appointmentId}/appointment")
    public APIResponse<PrescriptionResponse> getPrescriptionByAppointmentId(@PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionByAppointmentId(appointmentId))
                .build();
    }

    /**
     * @param prescriptionCreateRequest
     * @return
     */
    @PostMapping
    public APIResponse<PrescriptionResponse> createPrescription(@RequestBody PrescriptionCreateRequest prescriptionCreateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.createPrescription(prescriptionCreateRequest))
                .build();
    }

    /**
     * @param prescriptionUpdateRequest
     * @return
     */
    @PutMapping
    public APIResponse<PrescriptionResponse> updatePrescription(@RequestBody PrescriptionUpdateRequest prescriptionUpdateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.updatePrescription(prescriptionUpdateRequest))
                .build();
    }

    /**
     * @param prescriptionId
     * @return
     */
    @DeleteMapping("/{prescriptionId}")
    public APIResponse<String> deletePrescription(@PathVariable("prescriptionId") Long prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
        return APIResponse.<String>builder()
                .message("Delete prescription successfully")
                .build();
    }

}
