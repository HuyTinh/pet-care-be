package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Appointment;
import com.pet_care.manager_service.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT app FROM Appointment app " +
            " WHERE app.appointment_date = :date and app.status = true ORDER BY app.id DESC ")
    List<Appointment> getAllAppointmentToDay(@Param("date") LocalDate date);
    
//    @Query(value = "SELECT app FROM Appointment app " +
//            "    JOIN app.customer cts  " +
//            "    WHERE " +
//            "        (:searchQuery IS NULL OR (cts.first_name LIKE %:searchQuery% OR cts.last_name LIKE %:searchQuery%)) AND " +
//            "        (:appointment_date IS NULL OR app.appointment_date = :appointment_date) AND " +
//            "        (:status_accept IS NULL OR app.status_accept = :status_accept) AND " +
//            "        (:from_date IS NULL OR app.appointment_date >= :from_date) AND " +
//            "        (:to_date IS NULL OR app.appointment_date <= :to_date) " +
//            "    ORDER BY app.id DESC ")
//    List<Appointment> searchAppointmentDashboard(
//            @Param("appointment_date") LocalDate appointment_date,
//            @Param("status_accept") AppointmentStatus status_accept,
//            @Param("from_date") LocalDate from_date,
//            @Param("to_date") LocalDate to_date,
//            @Param("searchQuery") String searchQuery
//    );

    @Query(value = "SELECT app FROM Appointment app " +
            "    JOIN app.customer cts  " +
            "    WHERE " +
            "        (:searchQuery IS NULL OR (" +
            "                   cts.first_name LIKE %:searchQuery% " +
            "               OR cts.last_name LIKE %:searchQuery% )) AND " +
            "        (:appointment_date IS NULL OR app.appointment_date = :appointment_date) AND " +
            "        (:status_accept IS NULL OR app.status_accept = :status_accept) AND " +
            "        (:from_date IS NULL OR app.appointment_date >= :from_date) AND " +
            "        (:to_date IS NULL OR app.appointment_date <= :to_date) " +
            "    ORDER BY app.id DESC ")
    Page<Appointment> pageSearchAppointmentDashboard(
            @Param("appointment_date") LocalDate appointment_date,
            @Param("status_accept") AppointmentStatus status_accept,
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date,
            @Param("searchQuery") String searchQuery,
            Pageable pageable
    );

    @Query(value = "SELECT app FROM Appointment app WHERE app.appointment_date = :date and app.status = true ")
    List<Appointment> findByAppointmentYesterday(LocalDate date);
    
    @Query(value = "WITH Appointment_hours AS (    " +
            "                  SELECT 9 AS Hour, '09:00:00' AS Hour_appointment UNION ALL  " +
            "                  SELECT 10 AS Hour, '10:00:00' UNION ALL    " +
            "                  SELECT 11 AS Hour, '11:00:00' UNION ALL    " +
            "                  SELECT 12 AS Hour, '12:00:00' UNION ALL    " +
            "                  SELECT 13 AS Hour, '13:00:00' UNION ALL    " +
            "                  SELECT 14 AS Hour, '14:00:00' UNION ALL    " +
            "                  SELECT 15 AS Hour, '15:00:00'    " +
            "             ),    " +
            "             Appointment_Today AS (    " +
            "                 SELECT COUNT(app.id) as count_appointment, app.appointment_hour  " +
            "                 FROM appointments app  " +
            "                     WHERE app.status = true and app.appointment_date = :date  " +
            "                     GROUP BY app.appointment_hour    " +
            "             )  " +
            "             SELECT aph.Hour_appointment ,  " +
            "                    IFNULL(apt.count_appointment,0) as appointment   " +
            "                 FROM Appointment_hours aph    " +
            "                     LEFT JOIN Appointment_Today apt on aph.Hour_appointment = apt.appointment_hour  "
            , nativeQuery = true)
    List<Object[]> getAppointmentHoursToday(@Param("date") LocalDate date);

    @Query(value = "WITH Appointment_hours AS (    " +
            "                  SELECT 9 AS Hour, '09:00:00' AS Hour_appointment UNION ALL  " +
            "                  SELECT 10 AS Hour, '10:00:00' UNION ALL    " +
            "                  SELECT 11 AS Hour, '11:00:00' UNION ALL    " +
            "                  SELECT 12 AS Hour, '12:00:00' UNION ALL    " +
            "                  SELECT 13 AS Hour, '13:00:00' UNION ALL    " +
            "                  SELECT 14 AS Hour, '14:00:00' UNION ALL    " +
            "                  SELECT 15 AS Hour, '15:00:00'    " +
            "             ),    " +
            "             Appointment_Today AS (    " +
            "                 SELECT COUNT(app.id) as count_appointment, app.appointment_hour  " +
            "                 FROM appointments app  " +
            "                     WHERE app.status = true  " +
            "   AND (:from_date IS NULL OR app.appointment_date >= :from_date) " +
            "     AND (:to_date IS NULL OR app.appointment_date <= :to_date) " +
            "                     GROUP BY app.appointment_hour    " +
            "             ),  " +
            "            Sum_Appointment_Today AS (  " +
            "                SELECT COUNT(app.id) as sum_appointment  " +
            "                FROM appointments app  " +
            "                     WHERE app.status = true  " +
            "                   AND (:from_date IS NULL OR app.appointment_date >= :from_date) " +
            "                   AND (:to_date IS NULL OR app.appointment_date <= :to_date) " +
            "            )  " +
            "             SELECT aph.Hour_appointment ,  " +
            "                    IFNULL(apt.count_appointment,0) as appointment ,  " +
            "                    ROUND(IFNULL((apt.count_appointment/sat.sum_appointment) * 100 , 0),2) as percent  " +
            "                 FROM Appointment_hours aph    " +
            "                     LEFT JOIN Appointment_Today apt on aph.Hour_appointment = apt.appointment_hour  " +
            "                     CROSS JOIN Sum_Appointment_Today sat ", nativeQuery = true)
    Set<Object[]> getAppointmentChartFromAndToDate(
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date
    );

    @Query(value = "WITH Appointment_hours AS (    " +
            "                  SELECT 9 AS Hour, '09:00:00' AS Hour_appointment UNION ALL  " +
            "                  SELECT 10 AS Hour, '10:00:00' UNION ALL    " +
            "                  SELECT 11 AS Hour, '11:00:00' UNION ALL    " +
            "                  SELECT 12 AS Hour, '12:00:00' UNION ALL    " +
            "                  SELECT 13 AS Hour, '13:00:00' UNION ALL    " +
            "                  SELECT 14 AS Hour, '14:00:00' UNION ALL    " +
            "                  SELECT 15 AS Hour, '15:00:00'    " +
            "             ),    " +
            "             Appointment_Today AS (    " +
            "                 SELECT COUNT(app.id) as count_appointment, app.appointment_hour  " +
            "                 FROM appointments app  " +
            "                     WHERE app.status = true  " +
            "   AND (:month IS NULL OR MONTH(app.appointment_date) = :month) " +
            "     AND (:year IS NULL OR YEAR(app.appointment_date) = :year) " +
            "                     GROUP BY app.appointment_hour    " +
            "             ),  " +
            "            Sum_Appointment_Today AS (  " +
            "                SELECT COUNT(app.id) as sum_appointment  " +
            "                FROM appointments app  " +
            "                     WHERE app.status = true  " +
            "                   AND (:month IS NULL OR MONTH(app.appointment_date) = :month) " +
            "                   AND (:year IS NULL OR YEAR(app.appointment_date) = :year) " +
            "            )  " +
            "             SELECT aph.Hour_appointment ,  " +
            "                    IFNULL(apt.count_appointment,0) as appointment ,  " +
            "                    ROUND(IFNULL((apt.count_appointment/sat.sum_appointment) * 100 , 0),2) as percent  " +
            "                 FROM Appointment_hours aph    " +
            "                     LEFT JOIN Appointment_Today apt on aph.Hour_appointment = apt.appointment_hour  " +
            "                     CROSS JOIN Sum_Appointment_Today sat ", nativeQuery = true)
    Set<Object[]> getAppointmentChartMonthAndYear(
            @Param("month") Long month,
            @Param("year") Long year
    );
    
    @Query(value = " WITH Months AS ( " +
            "                               SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL " +
            "                               SELECT 2 AS Month, 'Feb' UNION ALL " +
            "                               SELECT 3 AS Month, 'Mar' UNION ALL " +
            "                               SELECT 4 AS Month, 'Apr' UNION ALL " +
            "                               SELECT 5 AS Month, 'May' UNION ALL " +
            "                               SELECT 6 AS Month, 'Jun' UNION ALL " +
            "                               SELECT 7 AS Month, 'Jul' UNION ALL " +
            "                               SELECT 8 AS Month, 'Aug' UNION ALL " +
            "                               SELECT 9 AS Month, 'Sep' UNION ALL " +
            "                               SELECT 10 AS Month, 'Oct' UNION ALL " +
            "                               SELECT 11 AS Month, 'Nov' UNION ALL " +
            "                               SELECT 12 AS Month, 'Dec'  ) " +
            "            , YEAR_FIRST AS ( " +
            "                SELECT DATE_FORMAT(app.appointment_date, '%b') AS MonthName, count(app.id) as count_year_first " +
            "                FROM appointments app " +
            "                WHERE app.status = true " +
            "                    AND year(app.appointment_date) = :year_first " +
            "                GROUP BY DATE_FORMAT(app.appointment_date, '%b') " +
            "            ), " +
            "                YEAR_SECOND AS ( " +
            "                    SELECT DATE_FORMAT(app.appointment_date, '%b') AS MonthName, count(app.id) as count_year_second " +
            "                    FROM appointments app " +
            "                    WHERE app.status = true " +
            "                      AND year(app.appointment_date) = :year_second " +
            "                    GROUP BY DATE_FORMAT(app.appointment_date, '%b') " +
            "                ) " +
            "        SELECT m.Month, m.MonthName " +
            "             , IFNULL(yf.count_year_first,0) as year_first " +
            "             ,IFNULL(ys.count_year_second,0) as year_second " +
            "             ,ROUND(IFNULL((yf.count_year_first/( IFNULL(yf.count_year_first,0) + IFNULL(ys.count_year_second,0))) * 100 , 0),2) as percent_year_first " +
            "             ,ROUND(IFNULL((ys.count_year_second/( IFNULL(yf.count_year_first,0) + IFNULL(ys.count_year_second,0))) * 100 , 0),2) as percent_year_second " +
            "        FROM Months m " +
            "                 LEFT JOIN YEAR_FIRST yf on yf.MonthName = m.MonthName " +
            "                 LEFT JOIN YEAR_SECOND ys on ys.MonthName = m.MonthName ", nativeQuery = true)
    Set<Object[]> getAppointmentYearFirstAndYearSecond(
            @Param("year_first") Long year_first,
            @Param("year_second") Long year_second
    );
}
