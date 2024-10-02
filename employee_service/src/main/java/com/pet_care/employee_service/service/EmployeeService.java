package com.pet_care.employee_service.service;

import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.exception.APIException;
import com.pet_care.employee_service.exception.ErrorCode;
import com.pet_care.employee_service.mapper.EmployeeMapper;
import com.pet_care.employee_service.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository employeeRepository;

    EmployeeMapper employeeMapper;

    public List<EmployeeResponse> getAllEmployee() {
        return employeeRepository
                .findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeResponse getEmployeeById(long id) {
        return employeeMapper.toDto(employeeRepository
                .findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND)));
    }
}
