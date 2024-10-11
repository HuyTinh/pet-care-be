package com.pet_care.employee_service.service;

import com.pet_care.employee_service.client.AccountClient;
import com.pet_care.employee_service.client.UploadImageClient;
import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.request.EmployeeUpdateRequest;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.exception.APIException;
import com.pet_care.employee_service.exception.ErrorCode;
import com.pet_care.employee_service.mapper.EmployeeMapper;
import com.pet_care.employee_service.model.Employee;
import com.pet_care.employee_service.repository.EmployeeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {
    EmployeeRepository employeeRepository;

    EmployeeMapper employeeMapper;

    UploadImageClient uploadImageClient;

    AccountClient accountClient;

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployee() {
        return employeeRepository
                .findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(long id) {
        return employeeMapper.toDto(employeeRepository
                .findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND)));
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest employeeCreateRequest, List<MultipartFile> files) {
        if (employeeRepository
                .getEmployeeByEmail(employeeCreateRequest.getEmail())
                .isEmpty()
        ) {
            if (!CollectionUtils.isEmpty(files)) {
                employeeCreateRequest.setImageUrl(uploadImageClient.uploadImage(files).get(0));
            }

            Employee employee = employeeMapper.toEntity(employeeCreateRequest);

            employee.setAccountId(accountClient
                    .createAccount(employeeCreateRequest)
                    .getData().getId());

            employee.setRole(employeeCreateRequest.getRoles().stream().toList().get(0));

            Employee savedEmployee = employeeRepository.save(employee);

            log.info("Employee saved: {}", savedEmployee);

            return employeeMapper.toDto(savedEmployee);

        }
        throw new APIException(ErrorCode.EMAIl_EXIST);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest employeeUpdateRequest, List<MultipartFile> files) {
        Employee existingEmployee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (!CollectionUtils.isEmpty(files)) {
            employeeUpdateRequest.setImageUrl(uploadImageClient.uploadImage(files).get(0));
        }

        employeeMapper.partialUpdate(employeeUpdateRequest, existingEmployee);

        return employeeMapper.toDto(employeeRepository
                .save(existingEmployee));

    }
}
