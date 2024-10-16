package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.client.MedicineClient;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.response.*;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.PetPrescriptionMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionDetailMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionMapper;
import com.pet_care.medical_prescription_service.model.*;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionService {
    private final PetPrescriptionMapper petPrescriptionMapper;
    @NotNull
    PrescriptionDetailMapper prescriptionDetailMapper;

    @NotNull PrescriptionRepository PrescriptionRepository;

    @NotNull PrescriptionMapper prescriptionMapper;

    @NotNull AppointmentClient appointmentClient;

    @NotNull
    PrescriptionRepository prescriptionRepository;

    @NotNull
    MedicineClient medicineClient;

    /**
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        return null;
    }

    /**
     * @param prescriptionId
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(@NotNull Long prescriptionId) {
        Prescription prescriptions = PrescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

        PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescriptions);

        prescriptionResponse.setAppointmentResponse(appointmentClient
                .getAppointmentById(prescriptions.getAppointmentId()).getData());

        log.info("Get prescription successful");

        return prescriptionResponse;
    }

    /**
     * @param prescriptionCreateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse createPrescription(@NotNull PrescriptionCreateRequest prescriptionCreateRequest) {
        Prescription newPrescription = prescriptionMapper
                .toEntity(prescriptionCreateRequest);

        List<PetPrescription> newPetPrescriptionList = prescriptionCreateRequest.getDetails().stream().map(petPrescriptionMapper::toEntity).toList();

        log.info("Create prescription successful");
        return null;
    }
}
