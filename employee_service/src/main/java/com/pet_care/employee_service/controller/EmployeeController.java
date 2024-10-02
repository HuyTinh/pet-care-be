package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {

    EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public Mono<APIResponse<EmployeeResponse>> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return employeeService.getEmployeeById(employeeId).map(employeeResponse ->
                APIResponse.<EmployeeResponse>builder()
                        .code(1000)
                        .data(employeeResponse)
                        .build());
    }

    @PostMapping()
    public Mono<APIResponse<EmployeeResponse>> createEmployee(@RequestBody EmployeeCreateRequest employeeCreateRequest) {
        return employeeService.createEmployee(employeeCreateRequest).map(employeeResponse ->
                APIResponse.<EmployeeResponse>builder()
                        .code(1000)
                        .data(employeeResponse)
                        .build());
    }

}
