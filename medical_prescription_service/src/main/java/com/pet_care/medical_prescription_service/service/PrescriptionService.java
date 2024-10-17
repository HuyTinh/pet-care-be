package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.client.MedicineClient;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.response.MedicinePrescriptionResponse;
import com.pet_care.medical_prescription_service.dto.response.PetPrescriptionResponse;
import com.pet_care.medical_prescription_service.dto.response.PrescriptionResponse;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.PetPrescriptionMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionDetailMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionMapper;
import com.pet_care.medical_prescription_service.model.PetPrescription;
import com.pet_care.medical_prescription_service.model.Prescription;
import com.pet_care.medical_prescription_service.repository.PetPrescriptionRepository;
import com.pet_care.medical_prescription_service.repository.PrescriptionDetailRepository;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @NotNull
    PetPrescriptionRepository petPrescriptionRepository;
    private final PrescriptionDetailRepository prescriptionDetailRepository;

    /**
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {

        return prescriptionRepository.findAll().stream().map(prescription -> {
            PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);

            prescriptionResponse.setAppointmentResponse(appointmentClient.getAppointmentById(prescription.getAppointmentId()).getData());

            prescriptionResponse.setDetails(
                    petPrescriptionRepository.findAllByPrescriptionId(prescription.getId()).stream().map(
                            petPrescription -> {
                                return PetPrescriptionResponse.builder()
                                        .pet(
                                                appointmentClient.getPetById(petPrescription.getPetId()).getData()
                                        )
                                        .note(petPrescription.getNote())
                                        .medicines(petPrescription.getMedicines().stream().map(prescriptionDetail ->
                                        {
                                            String medicine = medicineClient.getMedicineById(prescriptionDetail.getMedicineId()).getData().getName();


                                            String calculateUnit = medicineClient.getCalculationUnitById(prescriptionDetail.getCalculationId()).getData().getName();

                                            return MedicinePrescriptionResponse.builder()
                                                    .name(medicine)
                                                    .calculateUnit(calculateUnit)
                                                    .quantity(prescriptionDetail.getQuantity())
                                                    .build();
                                        }).collect(toSet()))
                                        .build();
                            }
                    ).collect(toSet()));

            return prescriptionResponse;
        }).collect(Collectors.toList());
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

        Prescription savePrescription = prescriptionRepository.save(newPrescription);

        List<PetPrescription> newPetPrescriptionList = prescriptionCreateRequest.getDetails().stream().map(petPrescriptionCreateRequest ->
                {
                    PetPrescription petPrescription = petPrescriptionMapper.toEntity(petPrescriptionCreateRequest);

                    petPrescription.setMedicines(petPrescriptionCreateRequest.getMedicines().stream().map(prescriptionDetailMapper::toEntity).collect(toSet()));

                    petPrescription.setPrescription(savePrescription);

                    return petPrescriptionRepository.save(petPrescription);
                })
                .peek(petPrescription -> {
                    prescriptionDetailRepository.saveAll(petPrescription.getMedicines());
                }).toList();


        newPetPrescriptionList.forEach(val -> {
            val.getMedicines().forEach(medicine -> {
                medicine.setPetPrescription(val);
            });
            prescriptionDetailRepository.saveAll(val.getMedicines()); // Sử dụng saveAll cho hiệu suất tốt hơn
        });

        return prescriptionMapper.toResponse(newPrescription);
    }
}
