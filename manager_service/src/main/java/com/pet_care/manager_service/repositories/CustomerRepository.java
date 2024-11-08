package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            "             p.name as pet_name, p.weight, p.age " +
            "            FROM customers ctm " +
            "               JOIN pets p on ctm.id = p.customer_id " +
            "               JOIN prescriptions prs on p.id = prs.pet_id " +
            "               JOIN appointments ap on ctm.id = ap.customer_id " +
            "               JOIN appointment_services aps  on ap.id = aps.appointment_id " +
            "            GROUP BY ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, " +
            "               p.name, p.weight, p.age"
            , nativeQuery = true )
    List<Object[]> getAllCustomer();

    @Query(value = "SELECT ctm.id, ctm.last_name, ctm.first_name, ctm.phone_number, ctm.email " +
            "            FROM customers ctm " +
            "            WHERE ctm.status = true AND " +
            "               (:searchQuery IS NULL OR (" +
            "                              ctm.first_name LIKE %:searchQuery% " +
            "                           OR ctm.last_name LIKE %:searchQuery% " +
            "                           OR ctm.phone_number LIKE %:searchQuery% " +
            "                           OR ctm.email LIKE %:searchQuery% )  )"
            , nativeQuery = true )
    Page<Object[]> getAllCustomerByStatusTrue(
            @Param("searchQuery") String search_query,
            Pageable pageable
    );
}
