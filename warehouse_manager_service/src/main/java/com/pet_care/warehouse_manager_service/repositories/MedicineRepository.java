package com.pet_care.warehouse_manager_service.repositories;

import com.pet_care.warehouse_manager_service.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
