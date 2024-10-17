package com.pet_care.warehouse_manager_service.repositories;

import com.pet_care.warehouse_manager_service.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
