package com.pet_care.medical_prescription_service.repository;

import com.pet_care.medical_prescription_service.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query(value = "SELECT * FROM medical_prescription_service.prescriptions ORDER BY id LIMIT 10000000 OFFSET 0", nativeQuery = true)
    List<Prescription> findAllCustom();

    Prescription findByAppointmentId(Long appointmentId);

    List<Prescription> findByCreatedAtBetween(Date startDate,  Date endDate);

    @Modifying
    @Transactional
    @Query(value = "UPDATE prescriptions set status = 'APPROVED' Where id = :id")
    int approvedPrescription(Long id);

    @Query(value = "Select * from prescriptions where DATE(created_at) = DATE(CURDATE()) and status = 'PENDING_PAYMENT'", nativeQuery = true)
    List<Prescription> findAllCurrentPrescriptionWithPendingStatus();
}
