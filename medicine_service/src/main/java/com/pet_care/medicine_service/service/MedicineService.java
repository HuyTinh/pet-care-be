package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
import com.pet_care.medicine_service.dto.response.MedicineResponse;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.mapper.MedicineMapper;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.repository.CalculationUnitRepository;
import com.pet_care.medicine_service.repository.LocationRepository;
import com.pet_care.medicine_service.repository.ManufactureRepository;
import com.pet_care.medicine_service.repository.MedicineRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineService {
    @NotNull MedicineRepository medicineRepository;

    @NotNull LocationRepository locationRepository;

    @NotNull CalculationUnitRepository calculationUnitRepository;

    @NotNull ManufactureRepository manufactureRepository;

    @NotNull MedicineMapper medicineMapper;

    /**
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicine() {
        List<MedicineResponse> medicineList = medicineRepository.findAll().stream().map(medicineMapper::toDto).collect(Collectors.toList());
        log.info("Find all medicine");
        return medicineList;
    }

    /**
     * @param id
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public Medicine getMedicineById(@NotNull Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));
        log.info("Find medicine by id: {}", medicine.getId());
        return medicine;
    }

    /**
     * @param medicineIds
     * @return
     */
    @NotNull
    @Transactional(readOnly = true)
    public List<Medicine> getMedicineInIds(@NotNull Set<Long> medicineIds) {
        List<Medicine> medicineList = medicineRepository.findAllById(medicineIds);
        log.info("Find medicine by ids: {}", medicineIds);
        return medicineList;
    }

    /**
     * @param medicineCreateRequest
     * @return
     */
    @NotNull
    @Transactional
    public Medicine createMedicine(@NotNull MedicineCreateRequest medicineCreateRequest) {
        Medicine newMedicine = medicineMapper.toEntity(medicineCreateRequest);

        newMedicine.setCalculationUnits(
                new HashSet<>(calculationUnitRepository
                        .findAllById(medicineCreateRequest.getCalculationUnits())));

        newMedicine.setLocations(
                new HashSet<>(locationRepository
                        .findAllById(medicineCreateRequest.getLocations())));

        Medicine savedMedicine = medicineRepository.save(newMedicine);

        log.info("Create medicine: {}", savedMedicine);

        return savedMedicine;
    }

    /**
     * @param medicineId
     * @param medicineUpdateRequest
     * @return
     */
    @NotNull
    @Transactional
    public Medicine updateMedicine(@NotNull Long medicineId, @NotNull MedicineUpdateRequest medicineUpdateRequest) {
        Medicine existingMedicine = medicineRepository.findById(medicineId).orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        existingMedicine.setCalculationUnits(
                new HashSet<>(calculationUnitRepository
                        .findAllById(medicineUpdateRequest.getCalculationUnits())));

        existingMedicine.setLocations(
                new HashSet<>(locationRepository
                        .findAllById(medicineUpdateRequest.getLocations())));

        medicineMapper.partialUpdate(medicineUpdateRequest, existingMedicine);

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);

        log.info("Update medicine: {}", updatedMedicine);

        return updatedMedicine;
    }

    /**
     * @param medicineId
     */
    @Transactional
    public void deleteMedicine(@NotNull Long medicineId) {
        medicineRepository.deleteById(medicineId);
        log.info("Delete medicine successful");
    }
}
