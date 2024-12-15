package com.pet_care.product_service.repository;

import com.pet_care.product_service.enums.StatusAccept;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM invoice i " +
            "WHERE (:note IS NULL OR LOWER(i.note) LIKE LOWER(CONCAT('%', :note, '%'))) " +
            "AND (:customerId IS NULL OR i.customer_id = :customerId) " +
            "AND (:statusAccept IS NULL OR i.statusAccept = :statusAccept) " +
            "AND (:minPrice IS NULL OR i.totalPrice >= :minPrice) " +
            "AND (:maxPrice IS NULL OR i.totalPrice <= :maxPrice) " +
            "AND (:startDate IS NULL OR i.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR i.createdAt <= :endDate)")
    Page<Invoice> findByFilters(
            @Param("note") String note,
            @Param("customerId") Long customerId,
            @Param("statusAccept") StatusAccept statusAccept,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );


}
