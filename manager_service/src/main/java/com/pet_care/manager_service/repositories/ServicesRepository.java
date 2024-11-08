package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    Optional<Services> findServicesByName(String name);

    @Query(value = "SELECT s.name as service_name " +
            "             FROM customers ctm " +
            "               LEFT JOIN pets p on ctm.id = p.customer_id " +
            "               LEFT JOIN species sc on p.species_id = sc.id " +
            "               LEFT JOIN prescriptions prs on p.id = prs.pet_id " +
            "               LEFT JOIN appointments ap on ctm.id = ap.customer_id " +
            "               LEFT JOIN appointment_services aps on ap.id = aps.appointment_id " +
            "               LEFT JOIN services s on aps.services_id = s.id " +
            "             WHERE ctm.id = :id " +
            "             GROUP BY s.name " , nativeQuery = true)
    List<Object[]> findServicesByCustomerId(@Param("id") Long id);

    @Query(value = "SELECT sv FROM Services sv WHERE sv.status = true ")
    List<Services> getAllServices();
    
    @Query(value = " " +
            "WITH PERCENT_REVENUE_SERVICE AS ( " +
            "    SELECT SUM(aps.price) as total " +
            "        FROM services s " +
            "            LEFT JOIN appointment_services aps on s.id = aps.services_id " +
            "            LEFT JOIN appointments app on aps.appointment_id = app.id " +
            "        WHERE app.status = true " +
            "             AND (:year IS NULL OR YEAR(app.appointment_date) = :year) " +
            "             AND (:month IS NULL OR MONTH(app.appointment_date) = :month) " +
            ") " +
            "SELECT " +
            "    main.id, " +
            "    main.name, " +
            "    IFNULL(main.count_service, 0) AS count_service, " +
            "    IFNULL(main.total, 0) AS total, " +
            "    ROUND(IFNULL((main.total/prs.total) * 100 , 0),2) percent_total " +
            "FROM ( " +
            "         SELECT s.id, s.name, COUNT(apps.services_id) AS count_service,  SUM(apps.price) AS total " +
            "         FROM " +
            "             services s " +
            "                 LEFT JOIN " +
            "             appointment_services apps ON s.id = apps.services_id " +
            "                 LEFT JOIN " +
            "             appointments app ON apps.appointment_id = app.id " +
            "                 AND app.status = true " +
            "         WHERE " +
            "            (:year IS NULL OR YEAR(app.appointment_date) = :year) " +
            "           AND (:month IS NULL OR MONTH(app.appointment_date) = :month) " +
            "         GROUP BY " +
            "             s.id, s.name " +
            " " +
            "         UNION ALL " +
            " " +
            "         SELECT " +
            "             s.id, s.name, 0 AS count_service, 0 AS total " +
            "         FROM " +
            "             services s " +
            "         WHERE " +
            "             s.status = true " +
            "           AND s.id NOT IN ( " +
            "             SELECT " +
            "                 s.id " +
            "             FROM " +
            "                 services s " +
            "                     LEFT JOIN " +
            "                 appointment_services apps ON s.id = apps.services_id " +
            "                     LEFT JOIN " +
            "                 appointments app ON apps.appointment_id = app.id " +
            "             WHERE " +
            "                 app.status = true " +
            "               AND (:month IS NULL OR MONTH(app.appointment_date) = :month) " +
            "               AND (:year IS NULL OR YEAR(app.appointment_date) = :year) " +
            "         ) " +
            "     ) AS main " +
            "    CROSS JOIN PERCENT_REVENUE_SERVICE prs " +
            "ORDER BY " +
            "    main.total DESC " +
            "LIMIT 10; ", nativeQuery = true)
    Set<Object[]> getTop10ServiceOfMonthSortCount(
            @Param("month") Long month,
            @Param("year") Long year
    );
    
    @Query(value = "WITH Months AS (          " +
            "                               SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL          " +
            "                               SELECT 2 AS Month, 'Feb' UNION ALL          " +
            "                               SELECT 3 AS Month, 'Mar' UNION ALL          " +
            "                               SELECT 4 AS Month, 'Apr' UNION ALL          " +
            "                               SELECT 5 AS Month, 'May' UNION ALL          " +
            "                               SELECT 6 AS Month, 'Jun' UNION ALL          " +
            "                               SELECT 7 AS Month, 'Jul' UNION ALL          " +
            "                               SELECT 8 AS Month, 'Aug' UNION ALL          " +
            "                               SELECT 9 AS Month, 'Sep' UNION ALL          " +
            "                               SELECT 10 AS Month, 'Oct' UNION ALL          " +
            "                               SELECT 11 AS Month, 'Nov' UNION ALL          " +
            "                               SELECT 12 AS Month, 'Dec'  )     " +
            "            , YEAR_FIRST AS (     " +
            "                SELECT DATE_FORMAT(app.appointment_date, '%b') AS MonthName, count(s.id) as count_year_first " +
            "                FROM services s " +
            "                    LEFT JOIN appointment_services aps on s.id = aps.services_id " +
            "                    LEFT JOIN appointments app on aps.appointment_id = app.id " +
            "                WHERE app.status = true     " +
            "                    AND year(app.appointment_date) = :year_first     " +
            "                GROUP BY DATE_FORMAT(app.appointment_date, '%b')     " +
            "            ),     " +
            "                YEAR_SECOND AS ( " +
            "                    SELECT DATE_FORMAT(app.appointment_date, '%b') AS MonthName, count(s.id) as count_year_second " +
            "                    FROM services s " +
            "                             LEFT JOIN appointment_services aps on s.id = aps.services_id " +
            "                             LEFT JOIN appointments app on aps.appointment_id = app.id " +
            "                    WHERE app.status = true " +
            "                      AND year(app.appointment_date) = :year_second " +
            "                    GROUP BY DATE_FORMAT(app.appointment_date, '%b') " +
            "                ) " +
            "            SELECT m.Month, m.MonthName     " +
            "                 , IFNULL(yf.count_year_first,0) as year_first " +
            "                 ,IFNULL(ys.count_year_second,0) as year_second " +
            "                 ,ROUND(IFNULL((yf.count_year_first/( IFNULL(yf.count_year_first,0) + IFNULL(ys.count_year_second,0))) * 100 , 0),2) as percent_year_first " +
            "                 ,ROUND(IFNULL((ys.count_year_second/( IFNULL(yf.count_year_first,0) + IFNULL(ys.count_year_second,0))) * 100 , 0),2) as percent_year_second " +
            "                FROM Months m     " +
            "                    LEFT JOIN YEAR_FIRST yf on yf.MonthName = m.MonthName " +
            "                    LEFT JOIN YEAR_SECOND ys on ys.MonthName = m.MonthName ", nativeQuery = true)
    Set<Object[]> getServiceYearFirstAndYearSecond(
            @Param("year_first") Long year_first,
            @Param("year_second") Long year_second
    );
}
