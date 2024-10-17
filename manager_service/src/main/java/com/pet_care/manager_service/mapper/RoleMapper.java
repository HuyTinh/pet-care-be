package com.pet_care.manager_service.mapper;

import com.pet_care.manager_service.dto.response.RoleResponse;
import com.pet_care.manager_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}
