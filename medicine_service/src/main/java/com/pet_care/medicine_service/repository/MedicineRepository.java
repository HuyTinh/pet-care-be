package com.pet_care.medicine_service.repository;

import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("SELECT m FROM medicines m JOIN m.manufacture manuf " +
            "WHERE (:searchTerm IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(manuf.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:manufacturingDate IS NULL OR m.manufacturingDate >= :manufacturingDate) " +
            "AND (:expiryDate IS NULL OR m.expiryDate <= :expiryDate) " +
            "AND (:status IS NULL OR m.status = :status) " +
            "AND m.types = :types " +
            "AND (:minPrice IS NULL OR m.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR m.price <= :maxPrice)")

    Page<Medicine> findByFilters(
            @Param("searchTerm") String searchTerm,
            @Param("manufacturingDate") Date manufacturingDate,
            @Param("expiryDate") Date expiryDate,
            @Param("status") MedicineStatus status,
            @Param("types") MedicineTypes types,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    @Query("select me from medicines me where me.types = 'MEDICINE'")
    List<Medicine> getAllMedicines();

    @Modifying
    @Transactional
    @Query(value = "UPDATE medicines set quantity = :newQuantity where id = :medicineId")
    int updateQuantity(@Param("medicineId") Long medicineId, @Param("newQuantity") Long newQuantity);

}
