package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.Prescription;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query(value = "select * from medical_prescription_service.prescriptions where id in (select  id from medical_prescription_service.prescriptions) LIMIT 1000000 OFFSET 0", nativeQuery = true)
    @NotNull
    List<Prescription> findAllCustom();

    Prescription findByAppointmentId(@NotNull Long appointmentId);
}
