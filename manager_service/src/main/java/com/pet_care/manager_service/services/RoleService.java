package com.pet_care.manager_service.services;

import com.pet_care.manager_service.dto.response.RoleResponse;
import com.pet_care.manager_service.mapper.RoleMapper;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAllRole();
}
