package com.pet_care.medical_prescription_service.controller;

import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.model.Prescription;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import com.pet_care.medical_prescription_service.service.PrescriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("prescription")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionController {

    @NotNull PrescriptionService prescriptionService;

    @NotNull PrescriptionRepository prescriptionRepository;

    /**
     * @return
     */
    @GetMapping
    public @NotNull APIResponse<List<PrescriptionResponse>> getAllPrescription() {
        return APIResponse.<List<PrescriptionResponse>>builder()
                .data(prescriptionService.getAllPrescriptions())
                .build();
    }

    /**
     * @param prescriptionId
     * @return
     */
    @GetMapping("/{prescriptionId}")
    public @NotNull APIResponse<PrescriptionResponse> getPrescriptionById(@NotNull @PathVariable("prescriptionId") Long prescriptionId) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionById(prescriptionId))
                .build();
    }


    /**
     * @param appointmentId
     * @return
     */
    @GetMapping("/{appointmentId}/appointment")
    public @NotNull APIResponse<PrescriptionResponse> getPrescriptionByAppointmentId(@NotNull @PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.getPrescriptionByAppointmentId(appointmentId))
                .build();
    }
    /**
     * @param prescriptionCreateRequest
     * @return
     */
    @PostMapping
    public @NotNull APIResponse<PrescriptionResponse> createPrescription(@NotNull @RequestBody PrescriptionCreateRequest prescriptionCreateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(prescriptionService.createPrescription(prescriptionCreateRequest))
                .build();
    }

    /**
     * @param prescriptionUpdateRequest
     * @return
     */
    @PutMapping
    public @NotNull APIResponse<PrescriptionResponse> updatePrescription(@NotNull @RequestBody PrescriptionUpdateRequest prescriptionUpdateRequest) {
        return APIResponse.<PrescriptionResponse>builder()
                .data(null)
                .build();
    }

    /**
     * @param prescriptionId
     * @return
     */
    @DeleteMapping("/{prescriptionId}")
    public APIResponse<String> deletePrescription(@NotNull @PathVariable("prescriptionId") Long prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
        return APIResponse.<String>builder()
                .message("Delete prescription successfully")
                .build();
    }

}
