package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.PetPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PetPrescriptionRepository extends JpaRepository<PetPrescription, Long> {

    List<PetPrescription> findAllByPrescriptionId(Long prescriptionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medical_prescription_service.pet_prescriptions WHERE pet_prescriptions.prescription_id = :prescriptionId ", nativeQuery = true)
    void deleteByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}
