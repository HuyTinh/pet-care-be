package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {
}
