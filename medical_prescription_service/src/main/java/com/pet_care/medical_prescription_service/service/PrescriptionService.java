package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.PrescriptionMapper;
import com.pet_care.medical_prescription_service.model.Appointment;
import com.pet_care.medical_prescription_service.model.Prescription;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionService {
    PrescriptionRepository PrescriptionRepository;

    PrescriptionMapper prescriptionMapper;

    AppointmentClient appointmentClient;

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        List<Prescription> prescriptions = PrescriptionRepository.findAll();

        List<PrescriptionResponse> prescriptionResponses = prescriptions.stream().
                map(prescription -> {
                    Appointment appointment = appointmentClient
                            .getAppointmentById(prescription.getAppointmentId());
                    PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);
                    prescriptionResponse.setAppointment(appointment);
                    return prescriptionResponse;
                })
                .toList();

        log.info("Get all prescriptions successful");

        return prescriptionResponses;
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(Long prescriptionId) {
        Prescription prescriptions = PrescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

        PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescriptions);

        prescriptionResponse.setAppointment(appointmentClient
                .getAppointmentById(prescriptions.getAppointmentId()));

        log.info("Get prescription successful");

        return prescriptionResponse;
    }
}
