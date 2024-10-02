package com.pet_care.identity_service.controller;


import com.pet_care.identity_service.dto.request.RoleCreationRequest;
import com.pet_care.identity_service.dto.request.RoleUpdateRequest;
import com.pet_care.identity_service.dto.response.ApiResponse;
import com.pet_care.identity_service.dto.response.RoleResponse;
import com.pet_care.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<RoleResponse> create(@RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @PutMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<RoleResponse> update(@PathVariable String role, @RequestBody RoleUpdateRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.update(role, request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<Void> delete(@PathVariable("role") String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }

}
