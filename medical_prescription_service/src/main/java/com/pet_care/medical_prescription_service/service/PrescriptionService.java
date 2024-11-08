package com.pet_care.medical_prescription_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.client.MedicineClient;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.*;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
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
import com.pet_care.medical_prescription_service.utils.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrescriptionService {

    PetPrescriptionMapper petPrescriptionMapper;


    PrescriptionDetailMapper prescriptionDetailMapper;


    PrescriptionRepository PrescriptionRepository;


    PrescriptionMapper prescriptionMapper;


    AppointmentClient appointmentClient;


    PrescriptionRepository prescriptionRepository;


    MedicineClient medicineClient;


    PetPrescriptionRepository petPrescriptionRepository;


    PrescriptionDetailRepository prescriptionDetailRepository;

    CacheService cacheService;

    RedisTemplate<String, Object> redisTemplate;

    /**
     * @return
     */

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        log.info("Fetching all prescriptions");

//        List<PrescriptionResponse> prescriptionResponseList =
//                (List<PrescriptionResponse>) cacheService.getCache("prescriptions");
//
//        if (prescriptionResponseList == null) {
        List<PrescriptionResponse> prescriptionResponseList = prescriptionRepository.findAllCustom().parallelStream()
                    .map(this::toPrescriptionResponse)
                    .collect(Collectors.toList());

//            cacheService.saveCache("prescriptions", prescriptionResponseList);
//        } else {
//            log.info("Retrieved {} prescriptions", prescriptionResponseList.size());
//        }

        return prescriptionResponseList;
    }

    /**
     * @param page
     * @param size
     * @param startDate
     * @param endDate
     * @return
     */

    @Transactional(readOnly = true)
    public PageableResponse<PrescriptionResponse> filteredPrescription(
            int page,
            int size,
            LocalDate startDate,
            LocalDate endDate,
            PrescriptionStatus prescriptionStatus
    ) {
        Date sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Date eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

//        ObjectMapper mapper = new ObjectMapper();

        prescriptionRepository.findByCreatedAtBetween(sDate, eDate, pageable);

        Page<PrescriptionResponse> prescriptionResponsePage = prescriptionRepository.findByCreatedAtBetween(sDate, eDate, pageable).map(this::toPrescriptionResponse);

//        Page<PrescriptionResponse> prescriptionPage = PaginationUtil
//                .convertListToPage(prescriptionResponseList, pageable);

        return PageableResponse.<PrescriptionResponse>builder()
                .content(prescriptionResponsePage.getContent())
                .pageNumber(prescriptionResponsePage.getPageable().getPageNumber())
                .pageSize(prescriptionResponsePage.getPageable().getPageSize())
                .totalPages(prescriptionResponsePage.getTotalPages())
                .build();

    }

    /**
     * @param prescriptionId
     * @return
     */

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(Long prescriptionId) throws JsonProcessingException {

//        ObjectMapper mapper = new ObjectMapper();
//
//        List<PrescriptionResponse> prescriptionResponseList =
//                (List<PrescriptionResponse>) cacheService.getCache("prescriptions");
//
//       PrescriptionResponse prescriptionResponse = null;
//
//        for (int i = 0; i < prescriptionResponseList.size(); i++ ) {
//            PrescriptionResponse pR = mapper.readValue(mapper.writeValueAsString(prescriptionResponseList.get(i)), PrescriptionResponse.class);
//            if(pR.getId().equals(prescriptionId)){
//                prescriptionResponse = pR;
//                break;
//            }
//        }

//        if (prescriptionResponse == null) {
            PrescriptionResponse prescriptionResponse = PrescriptionRepository.findById(prescriptionId)
                    .map(this::toPrescriptionResponse)
                    .orElseThrow(() -> {
                        log.error("Prescription with id {} not found", prescriptionId);
                        return new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND);
                    });
//        }
        return prescriptionResponse;
    }

    /**
     * @param prescriptionCreateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionCreateRequest prescriptionCreateRequest) {
        Prescription newPrescription = prescriptionMapper
                .toEntity(prescriptionCreateRequest);

//        List<PrescriptionResponse> objectList = (List<PrescriptionResponse>) redisTemplate.opsForValue().get("prescriptions");

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


        List<PrescriptionDetail> allMedicinesToSave = new ArrayList<>();

        newPetPrescriptionList.forEach(val -> {
            val.getMedicines().forEach(medicine -> {
                medicine.setPetPrescription(val);
                allMedicinesToSave.add(medicine);
            });
        });

        if (!allMedicinesToSave.isEmpty()) {
            prescriptionDetailRepository.saveAll(allMedicinesToSave);
        }

        PrescriptionResponse prescriptionResponse = toPrescriptionResponse(newPrescription);


        prescriptionResponse.setAppointmentResponse(appointmentClient
                .updateAppointmentService(prescriptionCreateRequest.getAppointmentId(), prescriptionCreateRequest.getServices()).getData());

//        if (objectList != null) {
//            objectList.add(prescriptionResponse);
//            cacheService.saveCache("prescriptions", objectList);
//        }

        return prescriptionResponse;
    }

    /**
     * @param prescriptionUpdateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse updatePrescription(PrescriptionUpdateRequest prescriptionUpdateRequest) {
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

                        if (petPrescriptionUpdateRequest.getId() != null) {
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

                                        if (prescriptionDetailUpdateRequest.getId() != null) {
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
    public PrescriptionResponse getPrescriptionByAppointmentId(Long appointmentId) {
        List<PrescriptionResponse> prescriptionResponseList =
                (List<PrescriptionResponse>) cacheService.getCache("prescriptions");

        PrescriptionResponse prescriptionResponse = null;

        if (prescriptionResponseList != null) {
            prescriptionResponseList.stream()
                    .filter(pR -> Objects.equals(pR.getAppointmentResponse().getId(), appointmentId)).findFirst()
                    .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        } else {
            prescriptionResponse = toPrescriptionResponse(prescriptionRepository
                    .findByAppointmentId(appointmentId));
        }

        return prescriptionResponse;
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
                                    medicineClient.getMedicineInIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getMedicineId).collect(toSet())).getData()
                            );

                            CompletableFuture<List<CalculationUnitResponse>> calculateFuture = prescriptionDetailsFuture.thenApply(prescriptionDetails ->
                                    medicineClient.getCalculationUnitByIds(prescriptionDetails.parallelStream().map(PrescriptionDetail::getCalculationId).collect(toSet())).getData()
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
                                            }).collect(toSet())
                                    )
                            );

                            return PetPrescriptionResponse.builder()
                                    .id(petPrescription.getId())
                                    .pet(petFuture.join())
                                    .note(petPrescription.getNote())
                                    .diagnosis(petPrescription.getDiagnosis())
                                    .medicines(medicinePrescriptionResponsesFuture.join())
                                    .build();
                        }).collect(toSet()));

        return CompletableFuture.allOf(appointmentFuture, petPrescriptionResponsesFuture).thenApply(v -> {
            PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);
            prescriptionResponse.setAppointmentResponse(appointmentFuture.join());
            prescriptionResponse.setDetails(petPrescriptionResponsesFuture.join());
            return prescriptionResponse;
        }).join();
    }
}
