package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Invoice_Service_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceServiceRepository extends JpaRepository<Invoice_Service_Detail, Long> {
    List<Invoice_Service_Detail> findByInvoiceId(Long invoice_id);
}
