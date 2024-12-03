package com.pet_care.medical_prescription_service.service;

import com.pet_care.medical_prescription_service.client.AppointmentClient;
import com.pet_care.medical_prescription_service.client.MedicineClient;
import com.pet_care.medical_prescription_service.dto.request.MedicineUpdateQtyRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionCreateRequest;
import com.pet_care.medical_prescription_service.dto.request.PrescriptionUpdateRequest;
import com.pet_care.medical_prescription_service.dto.response.*;
import com.pet_care.medical_prescription_service.entity.PetVeterinaryCare;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import com.pet_care.medical_prescription_service.exception.APIException;
import com.pet_care.medical_prescription_service.exception.ErrorCode;
import com.pet_care.medical_prescription_service.mapper.PetPrescriptionMapper;
import com.pet_care.medical_prescription_service.mapper.PetMedicineMapper;
import com.pet_care.medical_prescription_service.mapper.PetVeterinaryCareMapper;
import com.pet_care.medical_prescription_service.mapper.PrescriptionMapper;
import com.pet_care.medical_prescription_service.entity.PetMedicine;
import com.pet_care.medical_prescription_service.entity.PetPrescription;
import com.pet_care.medical_prescription_service.entity.Prescription;
import com.pet_care.medical_prescription_service.repository.PetPrescriptionRepository;
import com.pet_care.medical_prescription_service.repository.PetMedicineRepository;
import com.pet_care.medical_prescription_service.repository.PetVeterinaryCareRepository;
import com.pet_care.medical_prescription_service.repository.PrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
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

    PrescriptionRepository PrescriptionRepository;

    PrescriptionRepository prescriptionRepository;

    PetPrescriptionRepository petPrescriptionRepository;

    PetMedicineRepository petMedicineRepository;

    PetVeterinaryCareRepository petVeterinaryCareRepository;

    CacheService cacheService;

    RedisNativeService redisNativeService;

    RedisTemplate<String, Object> redisTemplate;

    PetPrescriptionMapper petPrescriptionMapper;

    PetMedicineMapper petMedicineMapper;

    PetVeterinaryCareMapper petVeterinaryCareMapper;

    PrescriptionMapper prescriptionMapper;

    AppointmentClient appointmentClient;

    MedicineClient medicineClient;

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
            PrescriptionStatus prescriptionStatus,
            Long accountId
    ) {
        Page<PrescriptionResponse> prescriptionResponsePage;

        List<PrescriptionResponse> cachePrescriptionResponses = redisNativeService.getRedisList("prescription-response-list", PrescriptionResponse.class);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if(!cachePrescriptionResponses.isEmpty()) {
            prescriptionResponsePage = convertListToPage(cachePrescriptionResponses.stream()
                            .filter(prescription -> !prescription.getCreatedAt().toInstant()            // Chuyển đổi Date thành Instant
                                    .atZone(ZoneId.systemDefault())  // Áp dụng múi giờ hệ thống
                                    .toLocalDate().isBefore(startDate) && !prescription.getCreatedAt().toInstant()            // Chuyển đổi Date thành Instant
                                    .atZone(ZoneId.systemDefault())  // Áp dụng múi giờ hệ thống
                                    .toLocalDate().isAfter(endDate))
                            .collect(Collectors.toList())
                    ,pageable);
        } else {
            cachePrescription();
            Date sDate = Date.from(startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            Date eDate = Date.from(endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

            prescriptionResponsePage = prescriptionRepository.findByCreatedAtBetween(sDate, eDate, pageable)
                    .map(this::toPrescriptionResponse);
        }


        return PageableResponse.<PrescriptionResponse>builder()
                .content(prescriptionResponsePage.getContent().stream().filter(
                        prescriptionResponse -> {
                            if(accountId != null) {
                                return prescriptionResponse.getAppointmentResponse().getAccountId().equals(accountId);
                            } else {
                                return true;
                            }
                        }
                ).toList())
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
    public PrescriptionResponse getPrescriptionById(Long prescriptionId) {
        List<PrescriptionResponse> cachePrescriptionResponses = redisNativeService.getRedisList("prescription-response-list", PrescriptionResponse.class);

        if(!cachePrescriptionResponses.isEmpty()) {
            return cachePrescriptionResponses.stream().filter(prescriptionResponse -> Objects.equals(prescriptionResponse.getId(), prescriptionId)).findFirst()
                    .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        } else {
            cachePrescription();
        }

        return PrescriptionRepository.findById(prescriptionId)
                .map(this::toPrescriptionResponse)
                .orElseThrow(() -> {
                    log.error("Prescription with id {} not found", prescriptionId);
                    return new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND);
                });
    }

    /**
     * @param prescriptionCreateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionCreateRequest prescriptionCreateRequest) {
        Prescription newPrescription = prescriptionMapper
                .toEntity(prescriptionCreateRequest);

        Prescription savePrescription = prescriptionRepository.save(newPrescription);

        CompletableFuture<Void> newPetPrescriptionListFuture = CompletableFuture.runAsync(() -> {
            prescriptionCreateRequest.getDetails().parallelStream().map(petPrescriptionCreateRequest ->
                    {
                        PetPrescription petPrescription = petPrescriptionMapper.toEntity(petPrescriptionCreateRequest);

                        petPrescriptionCreateRequest.getPetMedicines().forEach(petMedicineCreateRequest -> {
                            PetMedicine petMedicine = petMedicineMapper.toEntity(petMedicineCreateRequest);
                            petMedicine.setPetPrescription(petPrescription); // Thiết lập quan hệ ngược
                            petPrescription.getPetMedicines().add(petMedicine); // Thêm vào danh sách của PetPrescription
                        });

                        petPrescriptionCreateRequest.getPetVeterinaryCares().forEach(petVeterinaryCareCreateRequest -> {
                            PetVeterinaryCare petVeterinaryCare = petVeterinaryCareMapper.toEntity(petVeterinaryCareCreateRequest);
                            petVeterinaryCare.setPetPrescription(petPrescription); // Thiết lập quan hệ ngược
                            petPrescription.getPetVeterinaryCares().add(petVeterinaryCare); // Thêm vào danh sách của PetPrescription
                        });

                        petPrescription.setPrescription(savePrescription);

                        return petPrescriptionRepository.save(petPrescription);
                    })
                    .peek(petPrescription -> {
                        petPrescription.getPetMedicines().forEach(prescriptionDetail -> {
                            MedicineUpdateQtyRequest medicineUpdateQtyRequest = MedicineUpdateQtyRequest.builder()
                                    .medicineId(prescriptionDetail.getMedicineId())
                                    .qty(prescriptionDetail.getQuantity())
                                    .build();
                            medicineClient.updateQuantity(medicineUpdateQtyRequest);
                        });
                    });
        });
        cachePrescription();

        return newPetPrescriptionListFuture.thenApply(v -> toPrescriptionResponse(savePrescription)).join();
    }

    /**
     * @param prescriptionUpdateRequest
     * @return
     */
    @Transactional
    public PrescriptionResponse updatePrescription(Long prescriptionId, PrescriptionUpdateRequest prescriptionUpdateRequest) {
        Prescription existingPrescription = PrescriptionRepository.findById
                        (prescriptionId)
                .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

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

                            Set<PetMedicine> updatePetMedicines = petPrescriptionUpdateRequest
                                    .getPetMedicines().parallelStream()
                                    .map(prescriptionDetailUpdateRequest -> {

                                        PetMedicine petMedicine = petMedicineMapper
                                                .partialUpdate
                                                        (prescriptionDetailUpdateRequest, PetMedicine.builder().build());

                                        if (prescriptionDetailUpdateRequest.getId() != null) {
                                            PetMedicine existingPetMedicine = petMedicineRepository.findById(prescriptionDetailUpdateRequest.getId()).orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));

                                            petMedicine = petMedicineMapper.partialUpdate(prescriptionDetailUpdateRequest, existingPetMedicine);
                                        }

                                        petMedicine.setPetPrescription(finalUpdatePetPrescription);

                                        return petMedicine;
                                    }).collect(toSet());

                            updatePetPrescription.getPetMedicines().clear();

                            updatePetPrescription.getPetMedicines().addAll(updatePetMedicines);
                        }

                        return updatePetPrescription;
                    }).collect(toSet()));
        });

        cachePrescription();

        return prescriptionFuture.thenApply(v ->
                toPrescriptionResponse(prescriptionRepository.save(existingPrescription))).join();
    }

    /**
     * @param appointmentId
     * @return
     */
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionByAppointmentId(Long appointmentId) {
//        List<PrescriptionResponse> prescriptionResponseList =
//                (List<PrescriptionResponse>) cacheService.getCache("prescriptions");

        PrescriptionResponse prescriptionResponse = prescriptionResponse = toPrescriptionResponse(prescriptionRepository
                .findByAppointmentId(appointmentId));

//        if (prescriptionResponseList != null) {
//            prescriptionResponseList.stream()
//                    .filter(pR -> Objects.equals(pR.getAppointmentResponse().getId(), appointmentId)).findFirst()
//                    .orElseThrow(() -> new APIException(ErrorCode.PRESCRIPTION_NOT_FOUND));
//        } else {
//            prescriptionResponse = toPrescriptionResponse(prescriptionRepository
//                    .findByAppointmentId(appointmentId));
//        }

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

                            CompletableFuture<List<PetMedicine>> prescriptionDetailsFuture =
                                    CompletableFuture.supplyAsync(() -> new ArrayList<>(petPrescription.getPetMedicines()));

                            CompletableFuture<PetResponse> petFuture =
                                    CompletableFuture.supplyAsync(() -> appointmentClient
                                            .getPetById(petPrescription.getPetId()).getData());

                            CompletableFuture<List<MedicineResponse>> medicinesFuture = prescriptionDetailsFuture.thenApply(prescriptionDetails ->
                                    medicineClient.getMedicineInIds(prescriptionDetails.parallelStream().map(PetMedicine::getMedicineId).collect(toSet())).getData()
                            );

                            CompletableFuture<List<CalculationUnitResponse>> calculateFuture = prescriptionDetailsFuture.thenApply(prescriptionDetails ->
                                    medicineClient.getCalculationUnitByIds(prescriptionDetails.parallelStream().map(PetMedicine::getCalculationId).collect(toSet())).getData()
                            );

                            CompletableFuture<Set<PetMedicineResponse>> medicinePrescriptionResponsesFuture = prescriptionDetailsFuture.thenCompose(prescriptionDetails ->
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

                                                return PetMedicineResponse.builder()
                                                        .id(prescriptionDetail.getMedicineId())
                                                        .name(medicineName)
                                                        .calculateUnit(calculateName)
                                                        .quantity(prescriptionDetail.getQuantity())
                                                        .totalMoney(prescriptionDetail.getTotalMoney())
                                                        .build();
                                            }).collect(toSet())
                                    )
                            );

                           return CompletableFuture.allOf(petFuture,medicinePrescriptionResponsesFuture).thenApply(v -> PetPrescriptionResponse.builder()
                                    .id(petPrescription.getId())
                                    .pet(petFuture.join())
                                    .diagnosis(petPrescription.getDiagnosis())
                                    .veterinaryCares(petPrescription.getPetVeterinaryCares().stream().map(petVeterinaryCareMapper::toDto).collect(toSet()))
                                    .medicines(medicinePrescriptionResponsesFuture.join())
                                    .build()).join();
                        }).collect(toSet()));

        return CompletableFuture.allOf(appointmentFuture,petPrescriptionResponsesFuture).thenApply(v -> {
            PrescriptionResponse prescriptionResponse = prescriptionMapper.toResponse(prescription);
            prescriptionResponse.setAppointmentResponse(appointmentFuture.join());
            prescriptionResponse.setDetails(petPrescriptionResponsesFuture.join());
            return prescriptionResponse;
        }).join();
    }

    private void cachePrescription() {
        CompletableFuture.runAsync(() -> {
            redisNativeService.deleteRedisList("prescription-response-list");
            redisNativeService.saveToRedisList("prescription-response-list",prescriptionRepository.findAll().stream()
                    .map(this::toPrescriptionResponse)
                    .toList(),3600);
        });
    }

    private Page<PrescriptionResponse> convertListToPage(List<PrescriptionResponse> prescriptionResponseList, Pageable pageable) {
        // Tính toán index của phần tử bắt đầu và kết thúc trong trang
        int start = Math.min((int) pageable.getOffset(), prescriptionResponseList.size());
        int end = Math.min((start + pageable.getPageSize()), prescriptionResponseList.size());

        // Cắt danh sách để chỉ lấy các phần tử trong trang hiện tại
        List<PrescriptionResponse> pageContent = prescriptionResponseList.subList(start, end);

        // Trả về PageImpl (Page<PrescriptionResponse>)
        return new PageImpl<>(pageContent, pageable, prescriptionResponseList.size());
    }
}
