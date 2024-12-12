package com.pet_care.product_service.repository;

import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
