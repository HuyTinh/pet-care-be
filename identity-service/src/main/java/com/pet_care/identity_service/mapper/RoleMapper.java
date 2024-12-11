package com.pet_care.identity_service.mapper;

import com.pet_care.identity_service.dto.request.RoleCreationRequest;
import com.pet_care.identity_service.dto.response.RoleResponse;
import com.pet_care.identity_service.entity.Role;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    /**
     * @param roleCreationRequest
     * @return
     */
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleCreationRequest roleCreationRequest);

    /**
     * @param role
     * @return
     */
    RoleResponse toDto(Role role);

    /**
     * @param roleCreationRequest
     * @param role
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "permissions", ignore = true)
    Role partialUpdate(RoleCreationRequest roleCreationRequest, @MappingTarget Role role);
}