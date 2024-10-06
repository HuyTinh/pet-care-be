package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.model.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'CHECKED_IN' Where id = ?1")
    int checkInAppointment(Long id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'CANCELLED' Where id = ?1")
    int cancelledAppointment(Long id);

    List<Appointment> findAppointmentByStatus(AppointmentStatus status);

    @Query("SELECT ap from appointments ap WHERE ap.status in :statuses AND DATE(ap.appointmentDate) = DATE(:appointmentDate)")
    List<Appointment> findAppointmentByAppointmentDateAndStatusIn(@Param("appointmentDate") Date appointmentDate, @Param("statuses") Set<AppointmentStatus> statuses);

    @Query("SELECT ap from appointments ap WHERE ap.status = :status AND ap.accountId = :accountId")
    List<Appointment> findAppointmentByStatusAndAccountId(@Param("status") AppointmentStatus status, @Param("accountId") Long accountId, Sort sort);

    @Query(value = "SELECT EXISTS (SELECT TRUE FROM appointments WHERE status = 'CHECKED_IN' And id = ?1)")
    int checkInAppointmentIsExist(Long id);

    List<Appointment> findAllByAccountId(Long accountId);
}