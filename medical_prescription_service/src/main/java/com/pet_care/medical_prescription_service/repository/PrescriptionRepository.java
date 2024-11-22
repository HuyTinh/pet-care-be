package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query(value = "SELECT * FROM medical_prescription_service.prescriptions ORDER BY id LIMIT 10000000 OFFSET 0", nativeQuery = true)
    List<Prescription> findAllCustom();

    Prescription findByAppointmentId(Long appointmentId);

    Page<Prescription> findByCreatedAtBetween(Date startDate,  Date endDate, Pageable pageable);

    Page<Prescription> findByAppointmentIdInAndStatusInAndCreatedAtBetween(Set<Long> appointmentIds, Set<String> status, Date startDate,  Date endDate,Pageable pageable);

    Page<Prescription> findByAppointmentIdInAndStatusIn(Set<Long> appointmentIds, Set<String> status, Pageable pageable);
}
