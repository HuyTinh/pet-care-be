package com.pet_care.identity_service.service;

import com.pet_care.identity_service.dto.request.RoleCreationRequest;
import com.pet_care.identity_service.dto.request.RoleUpdateRequest;
import com.pet_care.identity_service.dto.response.RoleResponse;
import com.pet_care.identity_service.exception.APIException;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.mapper.PermissionMapper;
import com.pet_care.identity_service.mapper.RoleMapper;
import com.pet_care.identity_service.repository.PermissionRepository;
import com.pet_care.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    @NotNull RoleRepository roleRepository;

    @NotNull RoleMapper roleMapper;

    @NotNull PermissionMapper permissionMapper;

    @NotNull PermissionRepository permissionRepository;

    public RoleResponse create(@NotNull RoleCreationRequest request) {
        var role = roleMapper.toEntity(request);

        var listPermission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(listPermission));

        return roleMapper.toDto(roleRepository.save(role));
    }

    @NotNull
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
    }

    public RoleResponse getById(@NotNull String role) {
        return roleRepository.findById(role).map(roleMapper::toDto).orElseThrow(() -> new APIException(ErrorCode.ROLE_NOT_EXISTED));
    }

    public RoleResponse update(@NotNull String role, @NotNull RoleUpdateRequest request) {
        var updatedRole = roleRepository.findById(role).orElseThrow(() -> new APIException(ErrorCode.ROLE_NOT_EXISTED));

        var listPermission = permissionRepository.findAllById(request.getPermissions());
        updatedRole.setPermissions(new HashSet<>(listPermission));

        return roleMapper.toDto(roleRepository.save(updatedRole));
    }

    public void delete(@NotNull String role) {
        roleRepository.deleteById(role);
    }
}
