package com.pet_care.identity_service.controller;

import com.pet_care.identity_service.dto.request.PermissionRequest;
import com.pet_care.identity_service.dto.response.ApiResponse;
import com.pet_care.identity_service.dto.response.PermissionResponse;
import com.pet_care.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<List<PermissionResponse>> getAllPermission() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @PutMapping("/{permission}")
    ApiResponse<PermissionResponse> updatePermission(@PathVariable("permission") String permission, @RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.update(permission, permissionRequest))
                .build();
    }

    @DeleteMapping("/{permission}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    ApiResponse<Void> deletePermission(@PathVariable("permission") String permission) {
        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
