package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.entity.PetPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetPrescriptionRepository extends JpaRepository<PetPrescription, Long> {
    List<PetPrescription> findAllByPrescriptionId(Long prescriptionId);
}
