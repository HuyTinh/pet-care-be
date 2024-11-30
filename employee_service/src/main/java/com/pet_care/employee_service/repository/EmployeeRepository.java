package com.pet_care.employee_service.repository;

// Import necessary classes for JPA repository functionality, annotations, and optional handling
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository // Marks this interface as a repository component for Spring to handle dependency injection
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Retrieves an Employee by email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the Employee if found, otherwise empty
     */
    Optional<Employee> getEmployeeByEmail(String email);

    /**
     * Retrieves an Employee by account ID.
     *
     * @param accountId the account ID to search for
     * @return an Optional containing the Employee if found, otherwise empty
     */
    Optional<Employee> findByAccountId(Long accountId);

    @Modifying // Indicates that this is a modifying query, as it updates data
    @Transactional // Ensures the query executes within a transaction for rollback support if needed
    @Query(value = "UPDATE employees SET firstName = :firstName, lastName = :lastName, phoneNumber = :phoneNumber, gender = :gender, imageUrl = :imageUrl WHERE accountId = :accountId")
        // Defines a custom SQL query to update specific employee details where the id matches employeeId
    void softUpdateEmployee(
            @Param("accountId") Long accountId, // Maps method parameter employeeId to the query's :employeeId placeholder
            @Param("firstName") String firstName, // Maps method parameter firstName to the query's :firstName placeholder
            @Param("lastName") String lastName, // Maps method parameter lastName to the query's :lastName placeholder
            @Param("phoneNumber") String phoneNumber, // Maps method parameter phoneNumber to the query's :phoneNumber placeholder
            @Param("gender") Gender gender, // Maps method parameter gender to the query's :gender placeholder
            @Param("imageUrl") String imageUrl
    );
}
