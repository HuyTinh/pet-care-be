package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.RoleResponse;
import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.services.impl.RoleServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/management/role")
@Tag(name = "Role - Controller")
public class RoleController {
    @Autowired
    RoleServiceImpl roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> listRole = roleService.getAllRole();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get All Role" ,listRole));
    }
}
