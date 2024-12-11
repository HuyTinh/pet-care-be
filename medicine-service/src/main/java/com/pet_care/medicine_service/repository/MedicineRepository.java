package com.pet_care.medicine_service.repository;

// Import necessary Spring Data JPA and other dependencies
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.entity.Medicine;
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

/**
 * The MedicineRepository interface extends JpaRepository to provide CRUD operations for Medicine entities.
 * It includes custom queries for filtering and updating medicine records.
 */
@Repository  // Marks this interface as a Spring Data repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    /**
     * Custom query to search for medicines based on various filters.
     * Filters include search term, manufacturing date, expiry date, status, type, and price range.
     * Results are paginated based on the provided Pageable object.
     *
     * @param searchTerm Search term for medicine name or manufacturer name
     * @param manufacturingDate The minimum manufacturing date for filtering
     * @param expiryDate The maximum expiry date for filtering
     * @param status The status of the medicine (active/inactive)
     * @param types The type of the medicine (e.g., regular medicine, surgical tool)
     * @param minPrice The minimum price for filtering
     * @param maxPrice The maximum price for filtering
     * @param pageable Pagination parameters
     * @return A paginated list of medicines matching the filters
     */
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

    /**
     * Custom query to retrieve all medicines of type 'MEDICINE'.
     *
     * @return A list of all medicines that are of type 'MEDICINE'
     */
    @Query("select me from medicines me where me.types = 'MEDICINE'")
    List<Medicine> getAllMedicines();

    /**
     * Custom query to update the quantity of a specific medicine.
     * The quantity is updated based on the medicine's ID.
     *
     * @param medicineId The ID of the medicine whose quantity is to be updated
     * @param newQuantity The new quantity to set for the medicine
     * @return The number of rows affected (1 if successful, 0 otherwise)
     */
    @Modifying  // Indicates that this is a modifying query (UPDATE, DELETE, etc.)
    @Transactional  // Ensures that the update happens within a transaction
    @Query(value = "UPDATE medicines set quantity = :newQuantity where id = :medicineId")
    int updateQuantity(@Param("medicineId") Long medicineId, @Param("newQuantity") Long newQuantity);

}
