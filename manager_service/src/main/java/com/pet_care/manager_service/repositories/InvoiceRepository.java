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

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = " SELECT c.id  " +
            "FROM invoices i  " +
            "        LEFT JOIN pets p on i.pet_id = p.id " +
            "        LEFT JOIN customers c on c.id = p.customer_id " +
            "WHERE i.create_date = :date and i.status = true " +
            "GROUP BY c.id  ", nativeQuery = true)
    List<Object[]> getAllCustomerToday(@Param("date") LocalDate date);

    @Query(value = "SELECT  app.id " +
            "FROM appointments app " +
            "     LEFT JOIN appointment_services aps on app.id = aps.appointment_id " +
            "     LEFT JOIN services s on aps.services_id = s.id " +
            "     LEFT JOIN invoice_service_details isd on aps.id = isd.appointment_service_id " +
            "     LEFT JOIN invoices i on isd.invoice_id = i.id " +
            "WHERE i.create_date = :date and i.status = true ", nativeQuery = true)
    List<Object[]> getAllAppointmentToday(@Param("date") LocalDate date);

    @Query(value = "SELECT  i.id   " +
            " FROM Invoice i   "+
            " WHERE i.create_date = :date and i.status = true ")
    List<Object[]> getAllInvoiceToday(@Param("date") LocalDate date);

    @Query(value = "SELECT app FROM Appointment app WHERE app.appointment_date = :date and app.status = true ")
    List<Appointment> findByAppointmentDate(LocalDate date);
    
    @Query(value = "SELECT IFNULL(count(i.id),0) as count_invoice,IFNULL(sum(i.total),0) as count_revenue " +
            "    FROM Invoice i " +
            "    WHERE i.status = true " +
            "        AND (:from_date IS NULL OR i.create_date <= :from_date) " +
            "        AND (:to_date IS NULL OR i.create_date >= :to_date) " +
            "        AND (:create_date IS NULL OR i.create_date = :create_date) ")
    Optional<Object[]> getRevenueByDate(
            @Param("from_date") LocalDate from_date,
            @Param("to_date") LocalDate to_date,
            @Param("create_date") LocalDate create_date
            );
}
