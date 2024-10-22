package com.pet_care.medicine_service.repository;

import com.pet_care.medicine_service.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    @Query("SELECT m FROM medicines m WHERE "
            + "(:searchQuery IS NULL OR m.name LIKE %:searchQuery%) AND "
            + "(:manufacturingDate IS NULL OR m.manufacturingDate = :manufacturingDate) AND "
            + "(:expiryDate IS NULL OR m.expiryDate = :expiryDate) AND "
            + "(:status IS NULL OR m.status = :status) AND "
            + "(:minPrice IS NULL OR :maxPrice IS NULL OR m.price BETWEEN :minPrice AND :maxPrice)")
    List<Medicine> findByCriteria(
            @Param("manufacturingDate") Date manufacturingDate,
            @Param("expiryDate") Date expiryDate,
            @Param("status") MedicineStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("searchQuery") String searchQuery,
            Sort sort
    );

}
