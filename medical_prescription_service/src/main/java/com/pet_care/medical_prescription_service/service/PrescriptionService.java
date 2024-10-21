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

        PrescriptionResponse prescriptionResponse = toPrescriptionResponse(newPrescription);

        prescriptionResponse.setAppointmentResponse(appointmentClient.updateAppointmentService(prescriptionCreateRequest.getAppointmentId(), prescriptionCreateRequest.getServices()).getData());

        return prescriptionResponse;
    }

    /**
     * @param prescription
     * @return
     */
    private PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        CompletableFuture<AppointmentResponse> appointmentFuture = CompletableFuture.supplyAsync(() ->  appointmentClient.getAppointmentById(prescription.getAppointmentId()).getData());

        CompletableFuture<Set<PetPrescriptionResponse>> petPrescriptionResponses = CompletableFuture.supplyAsync(()-> petPrescriptionRepository.findAllByPrescriptionId(prescription.getId()).parallelStream().map(
                petPrescription -> {

                    List<PrescriptionDetail> prescriptionDetails = new ArrayList<>(petPrescription.getMedicines());

                    CompletableFuture<PetResponse> petFuture = CompletableFuture.supplyAsync(() -> appointmentClient.getPetById(petPrescription.getPetId()).getData());

                    CompletableFuture<List<MedicineResponse>> medicinesFuture = CompletableFuture.supplyAsync(() -> medicineClient.getMedicineInIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getMedicineId).collect(toSet())).getData());

                    CompletableFuture<List<CalculationUnitResponse>> calculateFuture = CompletableFuture.supplyAsync(() -> medicineClient.getCalculationUnitByIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getCalculationId).collect(toSet())).getData());

                    CompletableFuture<Set<MedicinePrescriptionResponse>> medicinePrescriptionResponses = CompletableFuture.supplyAsync(() -> prescriptionDetails.parallelStream().map(prescriptionDetail -> {

                        CompletableFuture<String> medicineNameFuture = CompletableFuture.supplyAsync(() -> medicinesFuture.join().parallelStream().filter(medicineResponse -> Objects.equals(medicineResponse.getId(), prescriptionDetail.getMedicineId())).findFirst().get().getName());

                        CompletableFuture<String> calculateNameFuture = CompletableFuture.supplyAsync(() -> calculateFuture.join().parallelStream().filter(calculationUnitResponse -> Objects.equals(calculationUnitResponse.getId(), prescriptionDetail.getCalculationId())).findFirst().get().getName());

                        return MedicinePrescriptionResponse.builder()
                                .id(prescriptionDetail.getMedicineId())
                                .name(medicineNameFuture.join())
                                .calculateUnit(calculateNameFuture.join())
                                .quantity(prescriptionDetail.getQuantity())
                                .build();

                    }).collect(Collectors.toSet()));

                    return PetPrescriptionResponse.builder()
                            .pet(petFuture.join())
                            .note(petPrescription.getNote())
                            .diagnosis(petPrescription.getDiagnosis())
                            .medicines(medicinePrescriptionResponses.join())
                            .build();
                }
        ).collect(toSet()));

        return CompletableFuture.allOf(appointmentFuture,petPrescriptionResponses).thenApply(v -> {
            CompletableFuture<PrescriptionResponse> prescriptionResponseFuture = CompletableFuture.supplyAsync(() -> prescriptionMapper.toResponse(prescription));

            return prescriptionResponseFuture.thenApply(prescriptionResponse -> {
                prescriptionResponse.setAppointmentResponse(appointmentFuture.join());
                prescriptionResponse.setDetails(petPrescriptionResponses.join());
                return  prescriptionResponse;
            }).join();
        }).join();
    }
}
