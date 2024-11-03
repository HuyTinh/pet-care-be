package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.*;
import com.pet_care.manager_service.services.impl.AppointmentServiceImpl;
import com.pet_care.manager_service.services.impl.DashboardServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("management/dashboard")
@Tag(name = "Dashboard - Controller")
public class DashboardController {

    @Autowired
    DashboardServiceImpl dashboardService;

    @Autowired
    AppointmentServiceImpl appointmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardHome() {
        DashboardResponse dashboardResponse = dashboardService.getDashboardHome();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Dashboard Success", dashboardResponse));
    }

    @GetMapping("/appointment-today")
    public ResponseEntity<ApiResponse<Set<AppointmentHomeDashboardTableResponse>>> getAppointmentToday() {
        Set<AppointmentHomeDashboardTableResponse> listApp = dashboardService.listAppointmentHomeDashboard();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Dashboard Success", listApp));
    }

    @GetMapping("/appointment-chart")
    public ResponseEntity<ApiResponse<List<AppointmentChartTodayResponse>>> getAppointmentChartHomeDashboard() {
        List<AppointmentChartTodayResponse> listApp = dashboardService.listAppointmentChartTodayResponse();
        return ResponseEntity.ok(new ApiResponse<>(2000, "Get Dashboard Success", listApp));
    }

    @GetMapping("/appointment-id/{id}")
    public ResponseEntity<ApiResponse<AppointmentHomeDashboardTableResponse>> getAppointmentByID(@PathVariable Long id) {
        AppointmentHomeDashboardTableResponse appointment = appointmentService.getAppointmentById(id);
        if(appointment == null){
            return ResponseEntity.ok(new ApiResponse<>(2000, "No have Appointment", appointment));
        }
        return ResponseEntity.ok(new ApiResponse<>(2000, "Find get Appointment", appointment));
    }
}
