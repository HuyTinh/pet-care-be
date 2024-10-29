package com.pet_care.manager_service.controllers;

import com.pet_care.manager_service.dto.response.ApiResponse;
import com.pet_care.manager_service.dto.response.AppointmentHomeDashboardTableResponse;
import com.pet_care.manager_service.entity.Appointment;
import com.pet_care.manager_service.services.impl.AppointmentServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("management/appointment")
@Tag(name = "Appointment - Controller")
public class AppointmentController {
    @Autowired
    AppointmentServiceImpl appointmentService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Set<AppointmentHomeDashboardTableResponse>>> searchAppointment(
            @RequestParam(required = false) LocalDate create_date,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String status_accept,
            @RequestParam(required = false) LocalDate from_date,
            @RequestParam(required = false) LocalDate to_date,
            @RequestParam(required = false) String search_query
    ) {
        Set<AppointmentHomeDashboardTableResponse> responses = appointmentService.searchAppointment(create_date, status, status_accept, from_date, to_date, search_query);
        if(responses.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(2000, "No have Appointment", responses));
        }
        return ResponseEntity.ok(new ApiResponse<>(2000, "Find get Appointment", responses));
    }
    @GetMapping("/yesterday")
    public ResponseEntity<ApiResponse<Set<AppointmentHomeDashboardTableResponse>>> searchAppointmentYesterday() {
        Set<AppointmentHomeDashboardTableResponse> responses = appointmentService.searchAppointmentYesterday();
        if(responses.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(2000, "No have Appointment", responses));
        }
        return ResponseEntity.ok(new ApiResponse<>(2000, "Find get Appointment", responses));
    }
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AppointmentHomeDashboardTableResponse>> getAppointmentByID(@PathVariable Long id) {
        AppointmentHomeDashboardTableResponse appointment = appointmentService.getAppointmentById(id);
        if(appointment == null){
            return ResponseEntity.ok(new ApiResponse<>(2000, "No have Appointment", appointment));
        }
        return ResponseEntity.ok(new ApiResponse<>(2000, "Find get Appointment", appointment));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<AppointmentHomeDashboardTableResponse>> deleteAppointment(@PathVariable Long id) {
        AppointmentHomeDashboardTableResponse appointment = appointmentService.deleteAppointment(id);
        if(appointment == null){
            return ResponseEntity.ok(new ApiResponse<>(2000, "No have Appointment", appointment));
        }
        return ResponseEntity.ok(new ApiResponse<>(2000, "Delete Appointment Successful", null));
    }
}
