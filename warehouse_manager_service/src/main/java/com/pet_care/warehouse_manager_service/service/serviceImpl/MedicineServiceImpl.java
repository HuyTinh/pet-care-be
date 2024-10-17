package com.pet_care.warehouse_manager_service.service.serviceImpl;

import com.pet_care.warehouse_manager_service.entity.Medicine;
import com.pet_care.warehouse_manager_service.repositories.MedicineRepository;
import com.pet_care.warehouse_manager_service.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicine() {
        return medicineRepository.findAll();
    }

    public <S extends Medicine> S create(S entity) {
        return medicineRepository.save(entity);
    }

    public <S extends Medicine> S update(S entity) {
        return medicineRepository.save(entity);
    }
}
