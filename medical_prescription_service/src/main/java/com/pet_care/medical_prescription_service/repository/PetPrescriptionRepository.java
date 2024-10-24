package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.PetPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface PetPrescriptionRepository extends JpaRepository<PetPrescription, Long> {

    List<PetPrescription> findAllByPrescriptionId(Long prescription_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medical_prescription_service.pet_prescriptions WHERE pet_id in :petIds ", nativeQuery = true)
    void deleteByPetIdIn(Iterable<Long> petIds);
}
