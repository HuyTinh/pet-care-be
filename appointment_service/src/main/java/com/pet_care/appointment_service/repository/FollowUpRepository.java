package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.model.FollowUp;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.Set;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

//    Page<FollowUp> findByAppointmentIdIn(Set<Long> appointmentIds, Pageable pageable);
//
//    Page<FollowUp> findByFollowUpDateBetween(Date startDate, Date endDate, Pageable pageable);
//
//    Page<FollowUp> findByFollowUpDateBetweenAndAppointmentIdIn(Date startDate, Date endDate, Set<Long> appointmentIds, Pageable pageable);
}
