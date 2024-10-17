package com.pet_care.manager_service.repositories;

import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
    
}
