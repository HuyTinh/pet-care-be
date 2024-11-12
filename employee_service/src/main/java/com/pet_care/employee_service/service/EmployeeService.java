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
import org.jetbrains.annotations.NotNull;
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

    /**
     * Repository for employee operations.
     */
    EmployeeRepository employeeRepository;

    /**
     * Mapper for converting between Employee entity and DTOs.
     */
    EmployeeMapper employeeMapper;

    /**
     * Client for handling image uploads.
     */
    UploadImageClient uploadImageClient;

    /**
     * Client for interacting with the account service.
     */
    AccountClient accountClient;

    /**
     * Retrieves all employees from the repository.
     * @return A list of EmployeeResponse DTOs.
     */
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployee() {
        return employeeRepository
                .findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an employee by their ID.
     * @param id The employee ID.
     * @return An EmployeeResponse DTO containing the employee details.
     */
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(long id) {
        return employeeMapper.toDto(employeeRepository
                .findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND)));
    }

    /**
     * Creates a new employee.
     * @param employeeCreateRequest The employee data to create.
     * @param files A list of files to upload (e.g., profile image).
     * @return The created EmployeeResponse DTO.
     */
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest employeeCreateRequest, List<MultipartFile> files) {
        if (employeeRepository
                .getEmployeeByEmail(employeeCreateRequest.getEmail())
                .isEmpty()) {
            // Upload image if provided
            if (!CollectionUtils.isEmpty(files)) {
                employeeCreateRequest.setImageUrl(uploadImageClient.uploadImage(files).get(0));
            }

            Employee employee = employeeMapper.toEntity(employeeCreateRequest);

            // Set account ID from the account service
            employee.setAccountId(accountClient
                    .createAccount(employeeCreateRequest)
                    .getData().getId());

            // Set the first role from the provided roles
            employee.setRole(employeeCreateRequest.getRoles().stream().toList().get(0));

            // Save employee to the repository
            Employee savedEmployee = employeeRepository.save(employee);

            log.info("Employee saved: {}", savedEmployee);

            return employeeMapper.toDto(savedEmployee);

        }
        throw new APIException(ErrorCode.EMAIL_EXIST);
    }

    /**
     * Updates an existing employee's details.
     * @param employeeId The ID of the employee to update.
     * @param employeeUpdateRequest The new employee data.
     * @param files A list of files to upload (e.g., profile image).
     * @return The updated EmployeeResponse DTO.
     */
    @Transactional
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeUpdateRequest employeeUpdateRequest, List<MultipartFile> files) {
        Employee existingEmployee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND));

        // Upload image if provided
        if (!CollectionUtils.isEmpty(files)) {
            employeeUpdateRequest.setImageUrl(uploadImageClient.uploadImage(files).get(0));
        }

        // Apply partial updates to the existing employee
        employeeMapper.partialUpdate(employeeUpdateRequest, existingEmployee);

        return employeeMapper.toDto(employeeRepository
                .save(existingEmployee));
    }

    /**
     * Retrieves an employee by their associated account ID.
     * @param accountId The account ID of the employee.
     * @return An EmployeeResponse DTO containing the employee details.
     */
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeByAccountId(Long accountId) {
        return employeeRepository.findByAccountId(accountId)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new APIException(ErrorCode.EMPLOYEE_NOT_FOUND));
    }
}
