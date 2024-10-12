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
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    @NotNull RoleService roleService;

    /**
     * @param request
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return APIResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    /**
     * @param role
     * @param request
     * @return
     */
    @PutMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<RoleResponse> updateRole(@PathVariable String role, @NotNull @RequestBody RoleUpdateRequest request) {
        return APIResponse.<RoleResponse>builder()
                .data(roleService.updateRole(role, request))
                .build();
    }

    /**
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<List<RoleResponse>> getAllRole() {
        return APIResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRole())
                .build();
    }

    /**
     * @param role
     * @return
     */
    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<Void> deleteRole(@PathVariable("role") String role) {
        roleService.deleteRole(role);
        return APIResponse.<Void>builder().build();
    }

}
