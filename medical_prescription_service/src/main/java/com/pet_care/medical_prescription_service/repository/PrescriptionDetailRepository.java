package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medical_prescription_service.prescription_details where prescription_details.pet_prescriptions_id in :petPrescriptionsIds", nativeQuery = true)
    void deleteAllByPetPrescriptionIdIn(@Param("petPrescriptionsIds") Iterable<Long> petPrescriptionsIds);

}
