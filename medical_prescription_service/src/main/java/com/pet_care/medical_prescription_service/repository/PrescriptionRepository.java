package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.Prescription;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query(value = "SELECT * FROM medical_prescription_service.prescriptions ORDER BY id LIMIT 10000000 OFFSET 0", nativeQuery = true)
    @NotNull
    List<Prescription> findAllCustom();

    Prescription findByAppointmentId(@NotNull Long appointmentId);

    Page<Prescription> findByCreatedAtBetween(Date startDate,
                                              Date endDate, Pageable pageable);
}
