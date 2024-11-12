package com.pet_care.customer_service.repository;

import com.pet_care.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and performing CRUD operations on the Customer entity.
 * Extends JpaRepository to provide common data access methods.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds a customer by their account ID.
     *
     * @param accountId the account ID of the customer
     * @return an Optional containing the Customer if found, or an empty Optional if not found
     */
    Optional<Customer> findByAccountId(Long accountId);
}
