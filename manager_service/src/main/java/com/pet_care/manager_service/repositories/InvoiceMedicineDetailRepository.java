package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Invoice;
import com.pet_care.manager_service.entity.Invoice_Medicine_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceMedicineDetailRepository extends JpaRepository<Invoice_Medicine_Detail, Long> {

    List<Invoice_Medicine_Detail> findByInvoice_id(Long invoice_id);
}
