package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.entity.FollowUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

//    Page<FollowUp> findByAppointmentIdIn(Set<Long> appointmentIds, Pageable pageable);
//
//    Page<FollowUp> findByFollowUpDateBetween(Date startDate, Date endDate, Pageable pageable);
//
//    Page<FollowUp> findByFollowUpDateBetweenAndAppointmentIdIn(Date startDate, Date endDate, Set<Long> appointmentIds, Pageable pageable);
}
