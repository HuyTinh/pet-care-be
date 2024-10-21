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

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * @param id
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'CHECKED_IN' Where id = :id")
    int checkInAppointment(@Param("id") Long id);

    /**
     * @param id
     * @return
     */

    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'CANCELLED' Where id = :id")
    int cancelledAppointment(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'APPROVED' Where id = :id")
    int approvedAppointment(@Param("id") Long id);

    /**
     * @param status
     * @return
     */
    List<Appointment> findAppointmentByStatus(AppointmentStatus status);

    /**
     * @param appointmentDate
     * @return
     */
    @Query("SELECT ap from appointments ap WHERE DATE(ap.appointmentDate) = DATE(:appointmentDate)")
    List<Appointment> findAppointmentByAppointmentDate(@Param("appointmentDate") Date appointmentDate);

    /**
     * @param appointmentDate
     * @param appointmentDate2
     * @return
     */
    List<Appointment> findByAppointmentDateBetween(Date appointmentDate, Date appointmentDate2);

    /**
     * @param status
     * @param accountId
     * @param sort
     * @return
     */
    @Query("SELECT ap from appointments ap WHERE ap.status = :status AND ap.accountId = :accountId")
    List<Appointment> findAppointmentByStatusAndAccountId(@Param("status") AppointmentStatus status, @Param("accountId") Long accountId, Sort sort);

    /**
     * @param id
     * @return
     */
    @Query(value = "SELECT EXISTS (SELECT TRUE FROM appointments WHERE status = 'CHECKED_IN' And id = ?1)")
    int checkInAppointmentIsExist(Long id);

    /**
     * @param accountId
     * @return
     */
    List<Appointment> findAllByAccountId(Long accountId);
}