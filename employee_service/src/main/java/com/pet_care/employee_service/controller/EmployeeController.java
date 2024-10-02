package com.pet_care.employee_service.controller;

import com.pet_care.employee_service.dto.request.EmployeeCreateRequest;
import com.pet_care.employee_service.dto.response.APIResponse;
import com.pet_care.employee_service.dto.response.EmployeeResponse;
import com.pet_care.employee_service.service.EmployeeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("employee")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {

    EmployeeService employeeService;

    @GetMapping
    public APIResponse<List<EmployeeResponse>> getAllEmployees() {
        return APIResponse.<List<EmployeeResponse>>builder().data(employeeService.getAllEmployee()).build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<EmployeeResponse> createEmployee(@ModelAttribute EmployeeCreateRequest employeeCreateRequest, @RequestPart("file") MultipartFile image) {
        System.out.println(employeeCreateRequest);
        System.out.println(image);
        return APIResponse.<EmployeeResponse>builder().data(employeeService.createEmployee(employeeCreateRequest)).build();
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
