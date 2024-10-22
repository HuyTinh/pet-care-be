package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.CustomerHomeDashboardResponse;
import com.pet_care.manager_service.dto.response.DashboardResponse;
import com.pet_care.manager_service.services.impl.DashboardServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("management/dashboard")
@Tag(name = "Dashboard - Controller")
public class DashboardController {

    @Autowired
    DashboardServiceImpl dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardHome() {
        DashboardResponse dashboardResponse = dashboardService.getHomeDashboard();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Dashboard Success", dashboardResponse));
    }

}
