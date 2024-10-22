package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

}
