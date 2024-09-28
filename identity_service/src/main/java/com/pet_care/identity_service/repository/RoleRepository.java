package com.pet_care.identity_service.repository;

import com.pet_care.identity_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

}