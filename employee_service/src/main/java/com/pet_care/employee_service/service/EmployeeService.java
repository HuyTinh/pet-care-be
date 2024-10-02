package com.pet_care.employee_service.service;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.exception.APIException;
import com.pet_care.employee_service.exception.ErrorCode;
import com.pet_care.employee_service.mapper.EmployeeMapper;
import com.pet_care.employee_service.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    public Mono<EmployeeResponse> getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(employeeMapper::toDto)
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.EMPLOYEE_NOT_FOUND)));
    }

    public Mono<EmployeeResponse> createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        return employeeRepository.save(employeeMapper.toEntity(employeeCreateRequest))
                .map(employeeMapper::toDto)
                .switchIfEmpty(Mono.error(new APIException(ErrorCode.EMPLOYEE_NOT_FOUND)));
    }

    public Flux<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().map(employeeMapper::toDto);
    }
}
