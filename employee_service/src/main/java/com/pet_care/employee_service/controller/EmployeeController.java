package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {



    @GetMapping("{employeeId}")
    APIResponse<Mono<EmployeeResponse>> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return APIResponse.builder().code(1000).data(Mono.just()).build();
    }

}
