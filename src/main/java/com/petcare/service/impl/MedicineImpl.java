package com.petcare.service.impl;

import com.petcare.entity.Medicine;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.MedicineRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineImpl implements EntityService<Medicine, Long> {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public List<Medicine> getAllEntity() {

        List<Medicine> medicines = ArrayMapper.mapperIterableToList(medicineRepository.findAll());

        return medicines;
    }

    @Override
    public Optional<Medicine> getEntityById(Long id) {

        Optional<Medicine> medicine = medicineRepository.findById(id);

        return medicine;
    }

    @Override
    public Medicine createEntity(Medicine medicine) {

        Optional<Medicine> medicineOptional = medicineRepository.findById(medicine.getId());

        if (medicineOptional.isPresent()) {
            throw new APIException(ErrorCode.MEDICINE_ALREADY_EXISTS);
        }

        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine updateEntity(Medicine medicine) {

        Medicine medicineOptional = medicineRepository.findById(medicine.getId())
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        return medicineRepository.save(medicine);
    }

    @Override
    public void deleteEntity(Long id) {

        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.MEDICINE_NOT_FOUND));

        medicineRepository.delete(medicine);

    }
}
