package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Service_Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<Service_Type, Long> {
}
