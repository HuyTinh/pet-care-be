package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.request.EmployeeUpdateRequest;
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

@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {

     EmployeeService employeeService;

    /**
     * @return
     */
    @GetMapping
    public APIResponse<List<EmployeeResponse>> getAllEmployees() {
        return APIResponse.<List<EmployeeResponse>>builder().data(employeeService.getAllEmployee()).build();
    }

    /**
     * @param employeeCreateRequest
     * @param files
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<EmployeeResponse> createEmployee( @ModelAttribute EmployeeCreateRequest employeeCreateRequest, @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        System.out.println(employeeCreateRequest);
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.createEmployee(employeeCreateRequest, files)).build();
    }

    /**
     * @param employeeId
     * @param employeeUpdateRequest
     * @param files
     * @return
     */
    @PutMapping(value = "/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<EmployeeResponse> updateEmployee(@PathVariable("employeeId")  Long employeeId,
                                                        @ModelAttribute  EmployeeUpdateRequest employeeUpdateRequest,
                                                        @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.updateEmployee(employeeId, employeeUpdateRequest, files)).build();
    }

    /**
     * @return
     */
    @DeleteMapping
    public APIResponse<EmployeeResponse> deleteEmployee() {
        return APIResponse.<EmployeeResponse>builder().message("Delete employee successful!").build();
    }

    /**
     * @param accountId
     * @return
     */
    @GetMapping("/account/{accountId}")
    public APIResponse<EmployeeResponse> getCustomerByAccountId(@PathVariable("accountId") Long accountId) {
        return APIResponse.<EmployeeResponse>builder()
                .data(employeeService.getEmployeeByAccountId(accountId))
                .build();
    }
}
