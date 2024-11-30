package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Repository // Marks this interface as a repository to be recognized by Spring Data JPA
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Updates the status of an appointment to 'CHECKED_IN'.
     *
     * @param id The id of the appointment to update
     * @return The number of affected rows (1 if successful)
     */
    @Modifying // Indicates that this is a modifying query (not a SELECT)
    @Transactional // Ensures the query is executed within a transaction
    @Query(value = "UPDATE appointments set status = 'CHECKED_IN' Where id = :id") // The SQL query to update the appointment status
    int checkInAppointment(@Param("id") Long id);

    /**
     * Updates the status of an appointment to 'CANCELLED'.
     *
     * @param id The id of the appointment to update
     * @return The number of affected rows (1 if successful)
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'CANCELLED' Where id = :id")
    int cancelledAppointment(@Param("id") Long id);

    /**
     * Updates the status of an appointment to 'APPROVED'.
     *
     * @param id The id of the appointment to update
     * @return The number of affected rows (1 if successful)
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'APPROVED' Where id = :id")
    int approvedAppointment(@Param("id") Long id);

    /**
     * Finds appointments by their status.
     *
     * @param status The status of the appointments to find
     * @return A list of appointments with the specified status
     */
    List<Appointment> findAppointmentByStatus(AppointmentStatus status);

    /**
     * Finds appointments by the appointment date.
     *
     * @param appointmentDate The date of the appointment
     * @return A list of appointments that match the given date
     */
    @Query("SELECT ap from appointments ap WHERE DATE(ap.appointmentDate) = DATE(:appointmentDate)") // Custom query to compare dates
    List<Appointment> findAppointmentByAppointmentDate(@Param("appointmentDate") Date appointmentDate);

    /**
     * Finds appointments between two dates and with statuses in a given set.
     *
     * @param appointmentDate The start date
     * @param appointmentDate2 The end date
     * @param statues The set of statuses to filter by
     * @param pageable The pagination parameters
     * @return A page of appointments that match the date range and statuses
     */
    Page<Appointment> findByAppointmentDateBetweenAndStatusIn(Date startDate,  Date endDate, Set<String> statues, Pageable pageable);

    /**
     * Finds appointments between two dates and with a specific status.
     *
     * @param appointmentDate The start date
     * @param appointmentDate2 The end date
     * @param statues The status to filter by
     * @return A list of appointments within the date range and with the specified status
     */
    List<Appointment> getByAppointmentDateBetweenAndStatus(Date startDate,  Date endDate, AppointmentStatus statues);


    Page<Appointment> findByAccountIdAndStatusIn(Long accountId,Set<String> statues, Pageable pageable);

    Page<Appointment> findAppointmentByAccountIdAndStatusInAndAppointmentDateBetween(Long accountId, Set<String> status, Date appointmentDate, Date appointmentDate2, Pageable pageable);

    /**
     * Finds appointments by status and account ID, with sorting.
     *
     * @param status The status of the appointments to find
     * @param accountId The account ID associated with the appointments
     * @param sort The sorting parameters
     * @return A list of appointments matching the status and account ID, sorted according to the provided criteria
     */
    @Query("SELECT ap from appointments ap WHERE ap.status = :status AND ap.accountId = :accountId")
    List<Appointment> findAppointmentByStatusAndAccountId(@Param("status") AppointmentStatus status, @Param("accountId") Long accountId, Sort sort);

    /**
     * Checks if an appointment with the given id exists and has the status 'CHECKED_IN'.
     *
     * @param id The id of the appointment
     * @return 1 if the appointment is 'CHECKED_IN', otherwise 0
     */
    @Query(value = "SELECT EXISTS (SELECT TRUE FROM appointments WHERE status = 'CHECKED_IN' And id = ?1)")
    int checkInAppointmentIsExist(Long id);

    /**
     * Finds all appointments by account ID.
     *
     * @param accountId The account ID associated with the appointments
     * @return A list of all appointments associated with the specified account ID
     */
    List<Appointment> findAllByAccountId(Long accountId);
}
