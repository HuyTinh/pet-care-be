package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.RoleCreationRequest;
import com.pet_care.identity_service.dto.response.RoleResponse;
import com.pet_care.identity_service.model.Role;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleCreationRequest roleCreationRequest);

    RoleResponse toDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "permissions", ignore = true)
    Role partialUpdate(RoleCreationRequest roleCreationRequest, @MappingTarget Role role);
}