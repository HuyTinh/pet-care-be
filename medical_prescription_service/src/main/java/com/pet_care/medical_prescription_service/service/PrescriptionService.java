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
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

        List<PrescriptionResponse> prescriptionResponseList = prescriptionRepository.findAll().stream().map(this::toPrescriptionResponse).collect(Collectors.toList());

        log.info("Get all prescriptions");

        return prescriptionResponseList;
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

        PrescriptionResponse prescriptionResponse = toPrescriptionResponse(prescriptions);

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

        return toPrescriptionResponse(newPrescription);
    }


    private PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);

        CompletableFuture<AppointmentResponse> appointmentFuture = CompletableFuture.supplyAsync(() ->
                appointmentClient.getAppointmentById(prescription.getAppointmentId()).getData());

        Set<PetPrescriptionResponse> petPrescriptionResponses = petPrescriptionRepository.findAllByPrescriptionId(prescription.getId()).stream().map(
                petPrescription -> {

                    CompletableFuture<PetResponse> petFuture = CompletableFuture.supplyAsync(() -> appointmentClient.getPetById(petPrescription.getPetId()).getData());

                    Set<MedicinePrescriptionResponse> medicinePrescriptionResponses = petPrescription.getMedicines().stream().map(prescriptionDetail -> {
                        CompletableFuture<String> medicineFuture = CompletableFuture.supplyAsync(() -> medicineClient.getMedicineById(prescriptionDetail.getMedicineId()).getData().getName());

                        CompletableFuture<String> calculateFuture = CompletableFuture.supplyAsync(() -> medicineClient.getCalculationUnitById(prescriptionDetail.getCalculationId()).getData().getName());

                        return CompletableFuture.allOf(medicineFuture, calculateFuture).thenApply(v -> MedicinePrescriptionResponse.builder()
                                .name(medicineFuture.join())
                                .calculateUnit(calculateFuture.join())
                                .quantity(prescriptionDetail.getQuantity())
                                .build()).join();

                    }).collect(Collectors.toSet());

                    return petFuture.thenApply(pet -> PetPrescriptionResponse.builder()
                            .pet(pet)
                            .note(petPrescription.getNote())
                            .medicines(medicinePrescriptionResponses)
                            .build()).join();
                }
        ).collect(toSet());

        prescriptionResponse.setAppointmentResponse(appointmentFuture.join());

        prescriptionResponse.setDetails(petPrescriptionResponses);

        return prescriptionResponse;
    }
}
