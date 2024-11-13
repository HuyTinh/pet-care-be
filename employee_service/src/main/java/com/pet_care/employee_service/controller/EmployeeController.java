package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.request.EmployeeUpdateRequest;
import com.pet_care.employee_service.dto.request.SoftEmployeeUpdateRequest;
import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller class to handle HTTP requests for managing employees.
 */
@RestController
@RequestMapping("employee")  // Base URL for all employee-related endpoints
@RequiredArgsConstructor  // Lombok annotation to generate constructor with required arguments
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)  // Set field access to private and final to ensure immutability
public class EmployeeController {

    // Service class injected into this controller to handle business logic
    EmployeeService employeeService;

    /**
     * Endpoint to get all employees.
     *
     * @return APIResponse containing a list of all employees
     */
    @GetMapping
    public APIResponse<List<EmployeeResponse>> getAllEmployees() {
        // Fetches the list of all employees and returns it wrapped in an APIResponse object
        return APIResponse.<List<EmployeeResponse>>builder()
                .data(employeeService.getAllEmployee())  // Fetches data from the service layer
                .build();  // Returns the response in APIResponse format
    }

    /**
     * Endpoint to create a new employee.
     *
     * @param employeeCreateRequest Object containing employee data to create
     * @param files                List of files to be uploaded (optional)
     * @return APIResponse containing the created employee data
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // Allows for file upload with multipart data
    public APIResponse<EmployeeResponse> createEmployee(
            @ModelAttribute EmployeeCreateRequest employeeCreateRequest,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) {

        // Logs the employee creation request (for debugging purposes)
        System.out.println(employeeCreateRequest);

        // Calls the service to create the employee and returns the response
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.createEmployee(employeeCreateRequest, files))  // Delegates to service layer
                .build();  // Returns the response in APIResponse format
    }

    /**
     * Endpoint to update an existing employee's data.
     *
     * @param employeeId           ID of the employee to update
     * @param employeeUpdateRequest Object containing updated employee data
     * @param files                List of files to upload (optional)
     * @return APIResponse containing the updated employee data
     */
    @PutMapping(value = "/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // Allows for file upload with multipart data
    public APIResponse<EmployeeResponse> updateEmployee(
            @PathVariable("employeeId") Long employeeId,
            @ModelAttribute EmployeeUpdateRequest employeeUpdateRequest,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) {

        // Calls the service to update the employee data and returns the response
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.updateEmployee(employeeId, employeeUpdateRequest, files))  // Delegates to service layer
                .build();  // Returns the response in APIResponse format
    }

    /**
     * Endpoint to delete an employee.
     *
     * @return APIResponse with a success message
     */
    @DeleteMapping
    public APIResponse<EmployeeResponse> deleteEmployee() {
        // Deletes the employee (logic for deleting not implemented here) and returns a success message
        return APIResponse.<EmployeeResponse>builder()
                .message("Delete employee successful!")  // Success message after deletion
                .build();  // Returns the response in APIResponse format
    }

    /**
     * Endpoint to get an employee by their associated account ID.
     *
     * @param accountId Account ID associated with the employee
     * @return APIResponse containing employee data associated with the account ID
     */
    @GetMapping("/account/{accountId}")
    public APIResponse<EmployeeResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        // Fetches employee based on the account ID and returns it wrapped in an APIResponse object
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.getEmployeeByAccountId(accountId))  // Fetches data from the service layer
                .build();  // Returns the response in APIResponse format
    }

    /**
     * Endpoint to perform a soft update on an employee's information.
     *
     * @param employeeId The unique identifier of the employee to update.
     * @param softEmployeeUpdateRequest The request object containing fields for the employee's soft update.
     * @return APIResponse containing the updated EmployeeResponse data.
     */
    @PutMapping("/account/{accountId}/soft-update")
    public APIResponse<EmployeeResponse> softUpdateEmployee(@PathVariable("accountId") Long accountId, @RequestBody SoftEmployeeUpdateRequest softEmployeeUpdateRequest) {
        // Fetches employee based on the account ID and returns it wrapped in an APIResponse object
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.softUpdateEmployee(accountId, softEmployeeUpdateRequest))
                .build();
    }

}
