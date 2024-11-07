package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Appointment;
import com.pet_care.manager_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = " SELECT c.id  " +
            "FROM invoices i  " +
            "        LEFT JOIN pets p on i.pet_id = p.id " +
            "        LEFT JOIN customers c on c.id = p.customer_id " +
            "WHERE i.create_date = :date and i.status = true " +
            "GROUP BY c.id  ", nativeQuery = true)
    List<Object[]> getAllCustomerToday(@Param("date") LocalDate date);

    @Query(value = "SELECT  i.id   " +
            " FROM Invoice i   "+
            " WHERE i.create_date = :date and i.status = true ")
    List<Object[]> getAllInvoiceToday(@Param("date") LocalDate date);

    @Query(value = " SELECT i " +
            " FROM Invoice i " +
            " WHERE i.create_date = :date and i.status = true ")
    List<Invoice> getTotalByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT app FROM Appointment app WHERE app.appointment_date = :date and app.status = true ")
    List<Appointment> findByAppointmentDate(LocalDate date);
    
    @Query(value = "SELECT IFNULL(count(i.id),0) as count_invoice,IFNULL(sum(i.total),0) as count_revenue " +
            "    FROM Invoice i " +
            "    WHERE i.status = true " +
            "        AND (:from_date IS NULL OR i.create_date >= :from_date) " +
            "        AND (:to_date IS NULL OR i.create_date <= :to_date) " +
            "        AND (:create_date IS NULL OR i.create_date = :create_date) ")
    Optional<Object[]> getRevenueByDate(
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date,
            @Param("create_date") LocalDate create_date
            );

    @Query(value = "SELECT i " +
            " FROM Invoice i " +
            " WHERE i.status  = true " +
            "   AND (:from_date IS NULL OR i.create_date >= :from_date) " +
            "   AND (:to_date IS NULL OR i.create_date <= :to_date) " +
            "   AND (:create_date IS NULL OR i.create_date = :create_date) " +
            " ORDER BY i.id DESC ")
    List<Invoice> getInvoiceByDate(
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date,
            @Param("create_date") LocalDate create_date
    );
    
    @Query(value = "WITH Months AS (  " +
            "    SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL  " +
            "    SELECT 2 AS Month, 'Feb' UNION ALL  " +
            "    SELECT 3 AS Month, 'Mar' UNION ALL  " +
            "    SELECT 4 AS Month, 'Apr' UNION ALL  " +
            "    SELECT 5 AS Month, 'May' UNION ALL  " +
            "    SELECT 6 AS Month, 'Jun' UNION ALL  " +
            "    SELECT 7 AS Month, 'Jul' UNION ALL  " +
            "    SELECT 8 AS Month, 'Aug' UNION ALL  " +
            "    SELECT 9 AS Month, 'Sep' UNION ALL  " +
            "    SELECT 10 AS Month, 'Oct' UNION ALL  " +
            "    SELECT 11 AS Month, 'Nov' UNION ALL  " +
            "    SELECT 12 AS Month, 'Dec'  " +
            "),  " +
            "    TotalInvoice AS (  " +
            "    SELECT DATE_FORMAT(i.create_date, '%b') AS MonthName,  " +
            "           IFNULL(SUM(i.Total), 0) AS Total  " +
            "    FROM invoices i  " +
            "    WHERE i.status = 1  " +
            "      AND YEAR(i.create_date) = COALESCE(:years, YEAR(CURDATE()))  " +
            "    GROUP BY DATE_FORMAT(i.create_date, '%b')  " +
            "),  " +
            "TotalAppointment AS (  " +
            "    SELECT DATE_FORMAT(app.appointment_date, '%b') AS MonthName,  " +
            "           COUNT(app.id) AS Appointments  " +
            "    FROM appointments app  " +
            "    WHERE app.status = 1  " +
            "      AND YEAR(app.appointment_date) = COALESCE(:years, YEAR(CURDATE()))  " +
            "    GROUP BY DATE_FORMAT(app.appointment_date, '%b')  " +
            ")  " +
            "SELECT m.Month, m.MonthName, IFNULL(ti.Total,0) AS Total, IFNULL(ta.Appointments,0) AS Appointments  " +
            "FROM Months m  " +
            "    LEFT JOIN TotalInvoice ti ON m.MonthName = ti.MonthName  " +
            "    LEFT JOIN TotalAppointment ta on m.MonthName = ta.MonthName  " +
            "ORDER BY m.Month  " , nativeQuery = true)
    List<Object[]> getInvoiceAndAppointmentByYear(@Param("years") Long year);
    
    @Query(value = 
            "WITH dates AS ( " +
            "    SELECT " +
            "        DATE(CONCAT(:year, '-', :month, '-01')) + INTERVAL (n) DAY AS date " +
            "    FROM ( " +
            "             SELECT @row := @row + 1 AS n " +
            "             FROM (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) a, " +
            "                  (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) b, " +
            "                  (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) c, " +
            "                  (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3) d, " +
            "                  (SELECT @row := -1) init " +
            "         ) numbers " +
            "    WHERE DATE(CONCAT(:year, '-', :month, '-01')) + INTERVAL (n) DAY <= LAST_DAY(DATE(CONCAT(:year, '-', :month, '-01'))) " +
            ") " +
            "SELECT " +
            "    DAY(d.date) AS DAY_INVOICE, " +
            "    IFNULL(SUM(i.total), 0) AS REVENUE " +
            "FROM " +
            "    dates d " +
            "        LEFT JOIN " +
            "    invoices i ON d.date = i.create_date " +
            "GROUP BY " +
            "    DAY_INVOICE " +
            "ORDER BY " +
            "    DAY(d.date); ", nativeQuery = true)
    Set<Object[]> getRevenueOfMonthAnhYear(
            @Param("year") Long year,
            @Param("month") Long month
    );

    @Query(value = "WITH Months AS (      " +
            "                   SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL      " +
            "                   SELECT 2 AS Month, 'Feb' UNION ALL      " +
            "                   SELECT 3 AS Month, 'Mar' UNION ALL      " +
            "                   SELECT 4 AS Month, 'Apr' UNION ALL      " +
            "                   SELECT 5 AS Month, 'May' UNION ALL      " +
            "                   SELECT 6 AS Month, 'Jun' UNION ALL      " +
            "                   SELECT 7 AS Month, 'Jul' UNION ALL      " +
            "                   SELECT 8 AS Month, 'Aug' UNION ALL      " +
            "                   SELECT 9 AS Month, 'Sep' UNION ALL      " +
            "                   SELECT 10 AS Month, 'Oct' UNION ALL      " +
            "                   SELECT 11 AS Month, 'Nov' UNION ALL      " +
            "                   SELECT 12 AS Month, 'Dec'      " +
            "               ),      " +
            "                   TotalInvoice AS (      " +
            "                   SELECT DATE_FORMAT(i.create_date, '%b') AS MonthName,      " +
            "                          IFNULL(SUM(i.Total), 0) AS Total      " +
            "                   FROM invoices i      " +
            "                   WHERE i.status = 1      " +
            "                     AND YEAR(i.create_date) = COALESCE(:year, YEAR(CURDATE())) " +
            "                   GROUP BY DATE_FORMAT(i.create_date, '%b')      " +
            "               ) " +
            "               SELECT m.Month, m.MonthName, IFNULL(ti.Total,0) AS Total " +
            "               FROM Months m      " +
            "                   LEFT JOIN TotalInvoice ti ON m.MonthName = ti.MonthName      " +
            "               ORDER BY m.Month " , nativeQuery = true)
    Set<Object[]> getRevenueYear(@Param("year") Long year);

    @Query(value = " WITH Months AS (         " +
            "                   SELECT 1 AS Month, 'Jan' AS MonthName UNION ALL         " +
            "                   SELECT 2 AS Month, 'Feb' UNION ALL         " +
            "                   SELECT 3 AS Month, 'Mar' UNION ALL         " +
            "                   SELECT 4 AS Month, 'Apr' UNION ALL         " +
            "                   SELECT 5 AS Month, 'May' UNION ALL         " +
            "                   SELECT 6 AS Month, 'Jun' UNION ALL         " +
            "                   SELECT 7 AS Month, 'Jul' UNION ALL         " +
            "                   SELECT 8 AS Month, 'Aug' UNION ALL         " +
            "                   SELECT 9 AS Month, 'Sep' UNION ALL         " +
            "                   SELECT 10 AS Month, 'Oct' UNION ALL         " +
            "                   SELECT 11 AS Month, 'Nov' UNION ALL         " +
            "                   SELECT 12 AS Month, 'Dec'  )    " +
            ", YEAR_FIRST AS (    " +
            "    SELECT DATE_FORMAT(i.create_date, '%b') AS MonthName, sum(i.total) as revenue_year_first    " +
            "    FROM invoices i    " +
            "    WHERE i.status = true    " +
            "        AND year(i.create_date) = :year_first    " +
            "    GROUP BY DATE_FORMAT(i.create_date, '%b')    " +
            "),    " +
            "    YEAR_SECOND AS (    " +
            "        SELECT DATE_FORMAT(i.create_date, '%b') AS MonthName, sum(i.total) as revenue_year_second    " +
            "        FROM invoices i    " +
            "        WHERE i.status = true    " +
            "          AND year(i.create_date) = :year_second    " +
            "        GROUP BY DATE_FORMAT(i.create_date, '%b')    " +
            "    )" +
            "SELECT m.Month, m.MonthName    " +
            "     , IFNULL(yf.revenue_year_first,0) as year_first    " +
            "     , IFNULL(ys.revenue_year_second,0) as year_second    " +
            "    FROM Months m    " +
            "    LEFT JOIN YEAR_FIRST yf on yf.MonthName = m.MonthName    " +
            "    LEFT JOIN YEAR_SECOND ys on ys.MonthName = m.MonthName    "
            , nativeQuery = true)
    Set<Object[]> getInvoiceYearFirstAndYearSecond(
            @Param("year_first") Long year_first,
            @Param("year_second") Long year_second
    );
}
