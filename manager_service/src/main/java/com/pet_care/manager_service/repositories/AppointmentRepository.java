package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT app FROM Appointment app " +
            " WHERE app.appointment_date = :date and app.status = true ORDER BY app.id DESC ")
    List<Appointment> getAllAppointmentToDay(@Param("date") LocalDate date);
    
    @Query(value = "SELECT app FROM Appointment app " +
            "    JOIN app.customer cts  " +
            "    WHERE " +
            "        (:searchQuery IS NULL OR (cts.first_name LIKE %:searchQuery% OR cts.last_name LIKE %:searchQuery%)) AND " +
            "        (:appointment_date IS NULL OR app.appointment_date = :appointment_date) AND " +
            "        (:status IS NULL OR app.status = :status) AND " +
            "        (:status_accept IS NULL OR app.status_accept = :status_accept) AND " +
            "        (:from_date IS NULL OR app.appointment_date >= :from_date) AND " +
            "        (:to_date IS NULL OR app.appointment_date <= :to_date) " +
            "    ORDER BY app.id DESC ")
    List<Appointment> searchAppointmentDashboard(
            @Param("appointment_date") LocalDate appointment_date,
            @Param("status") Boolean status,
            @Param("status_accept") String status_accept,
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date,
            @Param("searchQuery") String searchQuery
    );

    @Query(value = "SELECT app FROM Appointment app WHERE app.appointment_date = :date and app.status = true ")
    List<Appointment> findByAppointmentYesterday(LocalDate date);
    
    @Query(value = " WITH Appointment_hours AS ( " +
            "     SELECT 9 AS Hour, '9:00:00' AS Hour_appointment UNION ALL " +
            "     SELECT 10 AS Hour, '10:00:00' UNION ALL " +
            "     SELECT 11 AS Hour, '11:00:00' UNION ALL " +
            "     SELECT 12 AS Hour, '12:00:00' UNION ALL " +
            "     SELECT 13 AS Hour, '13:00:00' UNION ALL " +
            "     SELECT 14 AS Hour, '14:00:00' UNION ALL " +
            "     SELECT 15 AS Hour, '15:00:00' " +
            "), " +
            "Appointment_Today AS ( " +
            "    SELECT COUNT(app.id) as count_appointment, app.appointment_hour " +
            "        FROM appointments app " +
            "        WHERE app.status = true and app.appointment_date = :date " +
            "        GROUP BY app.appointment_hour " +
            ") " +
            "SELECT aph.Hour_appointment , IFNULL(apt.count_appointment,0) " +
            "    FROM Appointment_hours aph " +
            "        LEFT JOIN Appointment_Today apt on aph.Hour_appointment = apt.appointment_hour ", nativeQuery = true)
    List<Object[]> getAppointmentHoursToday(LocalDate date);
}
