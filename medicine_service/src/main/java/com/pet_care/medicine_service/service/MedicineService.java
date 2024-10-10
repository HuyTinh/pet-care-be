package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.dto.request.MedicineUpdateRequest;
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
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineService {
    MedicineRepository medicineRepository;

    LocationRepository locationRepository;

    CalculationUnitRepository calculationUnitRepository;

    ManufactureRepository manufactureRepository;

    MedicineMapper medicineMapper;

    public List<Medicine> getAllMedicine() {
        List<Medicine> medicineList = medicineRepository.findAll();
        log.info("Find all medicine");
        return medicineList;
    }

    public Medicine getMedicineById(Long id) {
        Medicine medicineList = medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));
        log.info("Find medicine by id: {}", medicineList.getId());
        return medicineList;
    }

    public Medicine createMedicine(MedicineCreateRequest medicineCreateRequest) {
        Medicine newMedicine = medicineMapper.toEntity(medicineCreateRequest);

        newMedicine.setCalculationUnits(
                new HashSet<>(calculationUnitRepository
                        .findAllById(medicineCreateRequest.getCalculationUnits())));

        newMedicine.setLocations(
                new HashSet<>(locationRepository
                        .findAllById(medicineCreateRequest.getLocations())));

        newMedicine.setManufactures(
                new HashSet<>(manufactureRepository
                        .findAllById(medicineCreateRequest.getManufactures())));

        Medicine savedMedicine = medicineRepository.save(newMedicine);

        log.info("Create medicine: {}", savedMedicine);

        return savedMedicine;
    }

    public Medicine updateMedicine(Long medicineId, MedicineUpdateRequest medicineUpdateRequest) {
        Medicine existingMedicine = medicineRepository.findById(medicineId).orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        existingMedicine.setCalculationUnits(
                new HashSet<>(calculationUnitRepository
                        .findAllById(medicineUpdateRequest.getCalculationUnits())));

        existingMedicine.setLocations(
                new HashSet<>(locationRepository
                        .findAllById(medicineUpdateRequest.getLocations())));

        existingMedicine.setManufactures(
                new HashSet<>(manufactureRepository
                        .findAllById(medicineUpdateRequest.getManufactures())));

        medicineMapper.partialUpdate(medicineUpdateRequest, existingMedicine);

        Medicine updatedMedicine = medicineRepository.save(existingMedicine);

        log.info("Update medicine: {}", updatedMedicine);

        return updatedMedicine;
    }

    public void deleteMedicine(Long medicineId) {
        medicineRepository.deleteById(medicineId);
        log.info("Delete medicine successful");
    }
}
