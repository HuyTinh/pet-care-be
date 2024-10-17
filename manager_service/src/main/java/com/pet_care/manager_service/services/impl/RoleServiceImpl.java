package com.pet_care.manager_service.services.impl;

import com.pet_care.manager_service.dto.response.RoleResponse;
import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.exception.AppException;
import com.pet_care.manager_service.exception.ErrorCode;
import com.pet_care.manager_service.mapper.RoleMapper;
import com.pet_care.manager_service.repositories.RoleRepository;
import com.pet_care.manager_service.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRole() {
        List<Role> listRole = roleRepository.findAll();
        if(listRole.isEmpty()){
            throw new AppException(ErrorCode.ROLE_IS_EMPTY);
        }
        return listRole.stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }
}
