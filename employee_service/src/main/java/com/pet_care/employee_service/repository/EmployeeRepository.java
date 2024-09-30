package com.pet_care.employee_service.repository;

import com.pet_care.employee_service.model.Employee;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends R2dbcRepository<Employee, Long> {
}
