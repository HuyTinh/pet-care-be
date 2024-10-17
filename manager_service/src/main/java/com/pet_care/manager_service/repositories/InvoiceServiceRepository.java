package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Invoice_Service_Detail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceServiceRepository extends JpaRepository<Invoice_Service_Detail, Long> {
}
