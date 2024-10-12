package com.pet_care.identity_service.controller;

import com.pet_care.identity_service.dto.request.PermissionRequest;
import com.pet_care.identity_service.dto.response.APIResponse;
import com.pet_care.identity_service.dto.response.PermissionResponse;
import com.pet_care.identity_service.service.PermissionService;
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
@RequiredArgsConstructor
@RequestMapping("permission")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    @NotNull PermissionService permissionService;

    /**
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<List<PermissionResponse>> getAllPermission() {
        return APIResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermission())
                .build();
    }

    /**
     * @param permissionRequest
     * @return
     */
    @PostMapping
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return APIResponse.<PermissionResponse>builder()
                .data(permissionService.createPermission(permissionRequest))
                .build();
    }

    /**
     * @param permission
     * @param permissionRequest
     * @return
     */
    @PutMapping("/{permission}")
    APIResponse<PermissionResponse> updatePermission(@PathVariable("permission") @NotNull String permission, @NotNull @RequestBody PermissionRequest permissionRequest) {
        return APIResponse.<PermissionResponse>builder()
                .data(permissionService.updatePermission(permission, permissionRequest))
                .build();
    }

    /**
     * @param permission
     * @return
     */
    @DeleteMapping("/{permission}")
    @PreAuthorize("hasRole('HOSPITAL_ADMINISTRATOR')")
    APIResponse<Void> deletePermission(@PathVariable("permission") @NotNull String permission) {
        permissionService.deletePermission(permission);
        return APIResponse.<Void>builder().build();
    }
}
