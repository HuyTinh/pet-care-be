package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
