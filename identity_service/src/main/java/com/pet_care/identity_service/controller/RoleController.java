package com.pet_care.identity_service.controller;


import com.pet_care.identity_service.dto.request.RoleCreationRequest;
import com.pet_care.identity_service.dto.request.RoleUpdateRequest;
import com.pet_care.identity_service.dto.response.APIResponse;
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
    APIResponse<RoleResponse> create(@RequestBody RoleCreationRequest request) {
        return APIResponse.<RoleResponse>builder()
                .data(roleService.create(request))
                .build();
    }

    @PutMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<RoleResponse> update(@PathVariable String role, @RequestBody RoleUpdateRequest request) {
        return APIResponse.<RoleResponse>builder()
                .data(roleService.update(role, request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<List<RoleResponse>> getAll() {
        return APIResponse.<List<RoleResponse>>builder()
                .data(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<Void> delete(@PathVariable("role") String role) {
        roleService.delete(role);
        return APIResponse.<Void>builder().build();
    }

}
