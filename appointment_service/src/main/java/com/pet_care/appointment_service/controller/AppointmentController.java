package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.service.AppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("appointment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {
    AppointmentService appointmentService;

    @GetMapping
    public APIResponse<List<AppointmentResponse>> getAllAppointment() throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointment())
                .build();
    }


    @GetMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.getAppointmentById(appointmentId))
                .build();
    }

    @GetMapping("present")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentPresent(@RequestParam("statuses") Set<AppointmentStatus> statuses) throws JsonProcessingException, ParseException {

        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentByAppointmentDateAndAndStatusIn(new Date(), statuses))
                .build();
    }

    @PostMapping
    public APIResponse<AppointmentResponse> createAppointment(@RequestBody AppointmentCreateRequest appointmentCreateRequest, @RequestParam(value = "emailNotification") boolean emailNotification) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.createAppointment(appointmentCreateRequest, emailNotification))
                .build();
    }

    @PutMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.updateAppointment(appointmentId, appointmentUpdateRequest))
                .build();
    }

    @PostMapping("/approved/{appointmentId}")
    public APIResponse<Integer> checkInAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.checkInAppointment(appointmentId))
                .build();
    }

    @PostMapping("/cancel/{appointmentId}")
    public APIResponse<Integer> cancelAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.cancelAppointment(appointmentId))
                .build();
    }

    @GetMapping("/account/{accountId}")
    public APIResponse<List<AppointmentResponse>> getAppointmentsByStatus(@PathVariable("accountId") Long accountId, @RequestParam("status") String status) throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatusAndAccountId(status, accountId))
                .build();
    }

    @GetMapping("/status/{status}")
    public APIResponse<List<AppointmentResponse>> getByStatusAndCustomerId(@PathVariable("status") String status) {

        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatus(status))
                .build();
    }

    @GetMapping("/isCheckin/{appointmentId}")
    public APIResponse<?> getAppointment(@PathVariable Long appointmentId) {
        return APIResponse.builder()
                .data(Map.of("isCheckIn:", appointmentService.checkInAppointment(appointmentId) == 1))
                .build();

    }
}
