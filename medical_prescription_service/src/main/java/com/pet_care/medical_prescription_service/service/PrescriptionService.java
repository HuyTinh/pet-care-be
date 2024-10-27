package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.client.MedicineClient;
import com.pet_care.medical_prescription_service.dto.request.PetPrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.*;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.PetPrescriptionMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionDetailMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionMapper;
import com.pet_care.medical_prescription_service.model.PetPrescription;
import com.pet_care.medical_prescription_service.model.Prescription;
import com.pet_care.medical_prescription_service.model.PrescriptionDetail;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionService {
    @NotNull
    PetPrescriptionMapper petPrescriptionMapper;

    @NotNull
    PrescriptionDetailMapper prescriptionDetailMapper;

    @NotNull
    PrescriptionRepository PrescriptionRepository;

    @NotNull
    PrescriptionMapper prescriptionMapper;

    @NotNull
    AppointmentClient appointmentClient;

    @NotNull
    PrescriptionRepository prescriptionRepository;

    @NotNull
    MedicineClient medicineClient;

    @NotNull
    PetPrescriptionRepository petPrescriptionRepository;

    @NotNull
    PrescriptionDetailRepository prescriptionDetailRepository;

    /**
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        List<PrescriptionResponse> prescriptionResponseList = prescriptionRepository.findAllCustom().parallelStream().map(prescription ->
        {
            CompletableFuture<PrescriptionResponse> responseCompletableFuture = CompletableFuture.supplyAsync(() -> toPrescriptionResponse(prescription));

            return responseCompletableFuture.join();
        }).collect(Collectors.toList());

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

        appointmentClient.approvedAppointment(prescriptionCreateRequest.getAppointmentId());

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
            prescriptionDetailRepository.saveAll(val.getMedicines());
        });

        PrescriptionResponse prescriptionResponse = toPrescriptionResponse(newPrescription);

        prescriptionResponse.setAppointmentResponse(appointmentClient
                .updateAppointmentService(prescriptionCreateRequest.getAppointmentId(), prescriptionCreateRequest.getServices()).getData());

        return prescriptionResponse;
    }

    /**
     * @param prescriptionUpdateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse updatePrescription(@NotNull PrescriptionUpdateRequest prescriptionUpdateRequest) {
        Prescription existingPrescription = PrescriptionRepository.findById
                        (prescriptionUpdateRequest.getId())
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

        appointmentClient.updateAppointmentService
                (prescriptionUpdateRequest.getId(), prescriptionUpdateRequest.getServices());

        CompletableFuture<Void> prescriptionFuture = CompletableFuture.runAsync(() -> {
            petPrescriptionRepository.saveAll(prescriptionUpdateRequest.getDetails()
                    .parallelStream().map(petPrescriptionUpdateRequest -> {
                PetPrescription updatePetPrescription = petPrescriptionMapper
                        .partialUpdate
                                (petPrescriptionUpdateRequest, PetPrescription.builder().build());

                if(petPrescriptionUpdateRequest.getId() != null){
                    PetPrescription existingPetPrescription = petPrescriptionRepository
                            .findById(petPrescriptionUpdateRequest.getId())
                            .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

                    updatePetPrescription = petPrescriptionMapper
                            .partialUpdate(petPrescriptionUpdateRequest, existingPetPrescription);

                    PetPrescription finalUpdatePetPrescription = updatePetPrescription;

                    Set<PrescriptionDetail> updatePrescriptionDetails = petPrescriptionUpdateRequest
                            .getMedicines().parallelStream()
                            .map(prescriptionDetailUpdateRequest -> {

                        PrescriptionDetail prescriptionDetail = prescriptionDetailMapper
                                .partialUpdate
                                        (prescriptionDetailUpdateRequest, PrescriptionDetail.builder().build());

                        if(prescriptionDetailUpdateRequest.getId() != null){
                            PrescriptionDetail existingPrescriptionDetail = prescriptionDetailRepository.findById(prescriptionDetailUpdateRequest.getId()).orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

                            prescriptionDetail = prescriptionDetailMapper.partialUpdate(prescriptionDetailUpdateRequest, existingPrescriptionDetail);
                        }

                        prescriptionDetail.setPetPrescription(finalUpdatePetPrescription);

                        return prescriptionDetail;
                    }).collect(toSet());

                    updatePetPrescription.getMedicines().clear();

                    updatePetPrescription.getMedicines().addAll(updatePrescriptionDetails);
                }

                return updatePetPrescription;
            }).collect(toSet()));
        });

        return prescriptionFuture.thenApply(v ->
                toPrescriptionResponse(prescriptionRepository.save(existingPrescription))).join();
    }
    /**
     * @param appointmentId
     * @return
     */
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionByAppointmentId(@NotNull Long appointmentId) {
        Prescription existingPrescription = prescriptionRepository
                .findByAppointmentId(appointmentId);

        return toPrescriptionResponse(existingPrescription);
    }

    /**
     * @param prescription
     * @return
     */
    private PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        CompletableFuture<AppointmentResponse> appointmentFuture =
                CompletableFuture.supplyAsync(() -> appointmentClient.getAppointmentById(prescription.getAppointmentId()).getData());

        CompletableFuture<Set<PetPrescriptionResponse>> petPrescriptionResponsesFuture =
                CompletableFuture.supplyAsync(() -> petPrescriptionRepository.findAllByPrescriptionId(prescription.getId()).parallelStream()
                        .map(petPrescription -> {

                            CompletableFuture<List<PrescriptionDetail>> prescriptionDetailsFuture =
                                    CompletableFuture.supplyAsync(() -> new ArrayList<>(petPrescription.getMedicines()));

                            CompletableFuture<PetResponse> petFuture =
                                    CompletableFuture.supplyAsync(() -> appointmentClient
                                            .getPetById(petPrescription.getPetId()).getData());

                            CompletableFuture<List<MedicineResponse>> medicinesFuture = prescriptionDetailsFuture.thenApply(prescriptionDetails ->
                                    medicineClient.getMedicineInIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getMedicineId).collect(Collectors.toSet())).getData()
                            );

                            CompletableFuture<List<CalculationUnitResponse>> calculateFuture = prescriptionDetailsFuture.thenApply(prescriptionDetails ->
                                    medicineClient.getCalculationUnitByIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getCalculationId).collect(Collectors.toSet())).getData()
                            );

                            CompletableFuture<Set<MedicinePrescriptionResponse>> medicinePrescriptionResponsesFuture = prescriptionDetailsFuture.thenCompose(prescriptionDetails ->
                                    CompletableFuture.allOf(medicinesFuture, calculateFuture).thenApply(v ->
                                            prescriptionDetails.parallelStream().map(prescriptionDetail -> {
                                                String medicineName = medicinesFuture.join().stream()
                                                        .filter(medicineResponse -> Objects.equals(medicineResponse.getId(), prescriptionDetail.getMedicineId()))
                                                        .findFirst()
                                                        .map(MedicineResponse::getName)
                                                        .orElse("Unknown Medicine"); // Handle default case

                                                String calculateName = calculateFuture.join().stream()
                                                        .filter(calculationUnitResponse -> Objects.equals(calculationUnitResponse.getId(), prescriptionDetail.getCalculationId()))
                                                        .findFirst()
                                                        .map(CalculationUnitResponse::getName)
                                                        .orElse("Unknown Unit"); // Handle default case

                                                return MedicinePrescriptionResponse.builder()
                                                        .id(prescriptionDetail.getMedicineId())
                                                        .name(medicineName)
                                                        .calculateUnit(calculateName)
                                                        .quantity(prescriptionDetail.getQuantity())
                                                        .totalMoney(prescriptionDetail.getTotalMoney())
                                                        .build();
                                            }).collect(Collectors.toSet())
                                    )
                            );

                            return PetPrescriptionResponse.builder()
                                    .id(petPrescription.getId())
                                    .pet(petFuture.join())
                                    .note(petPrescription.getNote())
                                    .diagnosis(petPrescription.getDiagnosis())
                                    .medicines(medicinePrescriptionResponsesFuture.join())
                                    .build();
                        }).collect(Collectors.toSet()));

        return CompletableFuture.allOf(appointmentFuture, petPrescriptionResponsesFuture).thenApply(v -> {
            PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);
            prescriptionResponse.setAppointmentResponse(appointmentFuture.join());
            prescriptionResponse.setDetails(petPrescriptionResponsesFuture.join());
            return prescriptionResponse;
        }).join();
    }
}
