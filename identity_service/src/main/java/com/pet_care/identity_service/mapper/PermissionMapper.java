package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.PermissionRequest;
import com.pet_care.identity_service.dto.response.PermissionResponse;
import com.pet_care.identity_service.model.Permission;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    /**
     * @param permissionRequest
     * @return
     */
    Permission toEntity(PermissionRequest permissionRequest);

    /**
     * @param permission
     * @return
     */
    PermissionResponse toDto(Permission permission);

    /**
     * @param permissionRequest
     * @param permission
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Permission partialUpdate(PermissionRequest permissionRequest, @MappingTarget Permission permission);
}