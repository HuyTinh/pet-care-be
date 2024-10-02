package com.pet_care.medicine_service.service;

import com.pet_care.medicine_service.dto.request.MedicineCreateRequest;
import com.pet_care.medicine_service.exception.APIException;
import com.pet_care.medicine_service.exception.ErrorCode;
import com.pet_care.medicine_service.model.Medicine;
import com.pet_care.medicine_service.repository.MedicineRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineService {
    MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicine() {
        return  medicineRepository.findAll();
    }

    public Medicine getMedicineById(Long id) {
        return  medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));
    }

    public Medicine addMedicine(MedicineCreateRequest medicineCreateRequest) {

    }
}
