package com.pet_care.employee_service.repository;

import com.pet_care.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> getEmployeeByEmail(String email);
}
