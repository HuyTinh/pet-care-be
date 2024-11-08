package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {


    @Query(value =
            "SELECT  main.id,  main.name, IFNULL(main.count_medicine, 0) AS count_medicine " +
            "FROM ( " +
            "         SELECT m.id, m.name , sum(prsd.quantity) as count_medicine " +
            "         FROM " +
            "             medicines m " +
            "                 LEFT JOIN " +
            "             prescription_details prsd ON m.id = prsd.medicine_id " +
            "                 LEFT JOIN " +
            "             prescriptions prs ON prsd.prescription_id = prs.id " +
            "         WHERE " +
            "             prs.status = true " +
            "             AND (:year IS NULL OR YEAR(prs.create_date) = :year) " +
            "             AND (:month IS NULL OR MONTH(prs.create_date) = :month) " +
            "         GROUP BY " +
            "             m.id, m.name " +
            "         UNION ALL " +
            "         SELECT " +
            "             m.id, m.name, 0 AS count_service " +
            "         FROM " +
            "             medicines m " +
            "         WHERE " +
            "              m.id NOT IN ( " +
            "                  SELECT m.id " +
            "                  FROM " +
            "                      medicines m " +
            "                          LEFT JOIN " +
            "                      prescription_details prsd ON m.id = prsd.medicine_id " +
            "                          LEFT JOIN " +
            "                      prescriptions prs ON prsd.prescription_id = prs.id " +
            "                  WHERE " +
            "                      prs.status = true " +
            "                    AND (:year IS NULL OR YEAR(prs.create_date) = :year) " +
            "                    AND (:month IS NULL OR MONTH(prs.create_date) = :month) " +
            "                  GROUP BY " +
            "                      m.id " +
            "         ) " +
            "     ) AS main " +
            "ORDER BY " +
            "    main.count_medicine DESC " +
            "LIMIT 10;", nativeQuery = true)
    Set<Object[]> getMedicineOfMonthAndYear(
            @Param("month") Long month,
            @Param("year") Long year
    );
    
    @Query(value = "WITH Months AS ( " +
            "    SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL " +
            "    SELECT 2 AS Month, 'Feb' UNION ALL " +
            "    SELECT 3 AS Month, 'Mar' UNION ALL " +
            "    SELECT 4 AS Month, 'Apr' UNION ALL " +
            "    SELECT 5 AS Month, 'May' UNION ALL " +
            "    SELECT 6 AS Month, 'Jun' UNION ALL " +
            "    SELECT 7 AS Month, 'Jul' UNION ALL " +
            "    SELECT 8 AS Month, 'Aug' UNION ALL " +
            "    SELECT 9 AS Month, 'Sep' UNION ALL " +
            "    SELECT 10 AS Month, 'Oct' UNION ALL " +
            "    SELECT 11 AS Month, 'Nov' UNION ALL " +
            "    SELECT 12 AS Month, 'Dec'  ) " +
            "        , YEAR_FIRST AS ( " +
            "    SELECT DATE_FORMAT(prs.create_date, '%b') AS MonthName, sum(prsd.price * prsd.quantity) as revenue_year_first " +
            "    FROM prescription_details prsd " +
            "        LEFT JOIN prescriptions prs ON prsd.prescription_id = prs.id " +
            "    WHERE prs.status = true " +
            "      AND year(prs.create_date) = :year_first " +
            "    GROUP BY DATE_FORMAT(prs.create_date, '%b') " +
            "), " +
            "     YEAR_SECOND AS ( " +
            "         SELECT DATE_FORMAT(prs.create_date, '%b') AS MonthName, sum(prsd.price * prsd.quantity) as revenue_year_second " +
            "         FROM prescription_details prsd " +
            "                  LEFT JOIN prescriptions prs ON prsd.prescription_id = prs.id " +
            "         WHERE prs.status = true " +
            "           AND year(prs.create_date) = :year_second " +
            "         GROUP BY DATE_FORMAT(prs.create_date, '%b') " +
            "     ) " +
            "SELECT m.Month, m.MonthName " +
            "     , IFNULL(yf.revenue_year_first,0) as year_first " +
            "     ,IFNULL(ys.revenue_year_second,0) as year_second " +
            "FROM Months m " +
            "         LEFT JOIN YEAR_FIRST yf on yf.MonthName = m.MonthName " +
            "         LEFT JOIN YEAR_SECOND ys on ys.MonthName = m.MonthName", nativeQuery = true)
    Set<Object[]> getMedicineYearFirstAndYearSecond(
            @Param("year_first") Long year_first,
            @Param("year_second") Long year_second
    );
}
