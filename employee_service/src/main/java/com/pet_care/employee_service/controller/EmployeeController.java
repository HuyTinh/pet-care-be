package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    @GetMapping
    public APIResponse<EmployeeResponse> getAllEmployees() {
        return APIResponse.<EmployeeResponse>builder().data(EmployeeResponse.builder().build()).build();
    }

    @PostMapping
    public APIResponse<EmployeeResponse> createEmployee() {
        return APIResponse.<EmployeeResponse>builder().data(EmployeeResponse.builder().build()).build();
    }

    @PutMapping
    public APIResponse<EmployeeResponse> updateEmployee() {
        return APIResponse.<EmployeeResponse>builder().data(EmployeeResponse.builder().build()).build();
    }

    @DeleteMapping
    public APIResponse<EmployeeResponse> deleteEmployee() {
        return APIResponse.<EmployeeResponse>builder().message("Delete employee successful!").build();
    }
}
