package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.entity.HospitalServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalServiceRepository extends JpaRepository<HospitalServiceEntity, String> {
}