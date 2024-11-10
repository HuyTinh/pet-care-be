package com.pet_care.bill_service.repository;

import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * @param payOsId
     * @param invoiceId
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE invoices set payosid = :payOsId Where id = :invoiceId", nativeQuery = true)
    int updatePaymentPayOSId(@Param("payOsId") String payOsId, @Param("invoiceId") Long invoiceId);

    @Query(value = "SELECT id from invoices where payosid = :payOsId", nativeQuery = true)
    Long getInvoiceIdByPayOSId(@Param("payOsId") String payOsId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE invoices set status = :status Where id = :invoiceId", nativeQuery = true)
    int changeStatus(@Param("invoiceId") Long invoiceId, @Param("status") InvoiceStatus status);

}
