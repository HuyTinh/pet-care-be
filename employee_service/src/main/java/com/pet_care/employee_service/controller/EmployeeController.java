package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {

EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public Mono<APIResponse<EmployeeResponse>> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return Mono.just(APIResponse.<EmployeeResponse>builder()
                .code(1000)
                .data(EmployeeResponse.builder().build())
                .build());
    }

}
