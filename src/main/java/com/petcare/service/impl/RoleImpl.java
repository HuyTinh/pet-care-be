package com.petcare.service.impl;

import com.petcare.entity.Role;
import com.petcare.exception.APIException;
import com.petcare.exception.ErrorCode;
import com.petcare.mapper.ArrayMapper;
import com.petcare.repository.RoleRepository;
import com.petcare.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleImpl implements EntityService<Role, Long> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllEntity() {
        return ArrayMapper.mapperIterableToList(roleRepository.findAll());
    }

    @Override
    public Optional<Role> getEntityById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role createEntity(Role role) {
        Optional<Role> roleOptional = roleRepository.findById(role.getId());
        if (roleOptional.isPresent()) {
            throw new APIException(ErrorCode.ROLE_ALREADY_EXISTS);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role updateEntity(Role role) {
        Role existingRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new APIException(ErrorCode.ROLE_NOT_FOUND));
        return roleRepository.save(role);
    }

    @Override
    public void deleteEntity(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.ROLE_NOT_FOUND));
        roleRepository.delete(role);
    }
}

