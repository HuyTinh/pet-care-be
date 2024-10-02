package com.pet_care.identity_service.service;

import com.pet_care.identity_service.dto.request.PermissionRequest;
import com.pet_care.identity_service.dto.response.PermissionResponse;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.exception.IdentityException;
import com.pet_care.identity_service.mapper.PermissionMapper;
import com.pet_care.identity_service.model.Permission;
import com.pet_care.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toEntity(request);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream().map(permissionMapper::toDto).collect(Collectors.toList());
    }

    public PermissionResponse update(String permission, PermissionRequest request) {
        Permission existPermission = permissionRepository.findById(permission).orElseThrow(() -> new IdentityException(ErrorCode.PERMISSION_NOT_FOUND));

        existPermission.setDescription(request.getDescription());

        existPermission.setName(request.getName());

        return permissionMapper.toDto(permissionRepository.save(existPermission));
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }

}
