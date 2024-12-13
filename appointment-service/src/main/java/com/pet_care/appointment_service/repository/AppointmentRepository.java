package com.pet_care.appointment_service.repository;

import com.pet_care.appointment_service.dto.response.ReportAppointmentByDateToDateResponse;
import com.pet_care.appointment_service.dto.response.ReportAppointmentByYearResponse;
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
    List<Appointment> findByAppointmentDateBetween(Date startDate,  Date endDate);

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

    @Modifying
    @Transactional
    @Query(value = "UPDATE appointments set status = 'NO_SHOW' Where appointmentDate < CURDATE() and status = 'SCHEDULED'")
    int updateNoShowStatusAppointment();


    @Query(value = "SELECT ap FROM appointments ap WHERE appointmentDate BETWEEN CURDATE() - INTERVAL 3 DAY AND CURDATE()", nativeQuery = true)
    List<Appointment> getAppointmentsUpcoming();

    @Query(value = "SELECT" +
            "    DATE_FORMAT(appointment_date, '%m-%Y') AS month,\n" +
            "    COUNT(CASE WHEN status = 'SCHEDULED' THEN 1 END) AS numberOfScheduled,\n" +
            "    COUNT(CASE WHEN status = 'APPROVED' THEN 1 END) AS numberOfApproved,\n" +
            "    COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END) AS numberOfCancelled,\n" +
            "    COUNT(CASE WHEN status = 'NO_SHOW' THEN 1 END) AS numberOfNoShow,\n" +
            "    COUNT(id) AS totalAppointments\n" +
            "FROM\n" +
            "    appointments\n" +
            "WHERE\n" +
            "    appointment_date BETWEEN CONCAT(:year, '-01-01') AND CONCAT(:year, '-12-31')\n" +
            "GROUP BY\n" +
            "    DATE_FORMAT(appointment_date, '%m-%Y')\n" +
            "LIMIT 10000000", nativeQuery = true)
    List<ReportAppointmentByYearResponse> getAppointmentsReportByYear(@Param("year") int year);

    @Query(value = "SELECT\n" +
            "    appointment_date AS date,\n" +
            "    COUNT(CASE WHEN status = 'SCHEDULED' THEN 1 END) AS numberOfScheduled,\n" +
            "    COUNT(CASE WHEN status = 'APPROVED' THEN 1 END) AS numberOfApproved,\n" +
            "    COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END) AS numberOfCancelled,\n" +
            "    COUNT(CASE WHEN status = 'NO_SHOW' THEN 1 END) AS numberOfNoShow,\n" +
            "    COUNT(id) AS totalAppointments\n" +
            "    FROM\n" +
            "            appointments\n" +
            "    WHERE\n" +
            "    appointment_date BETWEEN :startDate AND :endDate \n" +
            "    GROUP BY\n" +
            "    appointment_date\n" +
            "    LIMIT 10000000", nativeQuery = true)
    List<ReportAppointmentByDateToDateResponse> getAppointmentsReportByDateToDate(@Param("startDate") Date startDate,
                                                                                           @Param("endDate") Date endDate);
}
