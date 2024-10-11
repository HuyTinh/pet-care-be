package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.service.AppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("appointment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {
    @NotNull AppointmentService appointmentService;

    @GetMapping
    public APIResponse<List<AppointmentResponse>> getAllAppointment(@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointment())
                .build();
    }

    @GetMapping("/filter")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentByStartDateAndEndDate(
            @NotNull @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @NotNull @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "statues", required = false) Set<String> statues) throws JsonProcessingException {

        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.filterAppointments(startDate, endDate, statues))
                .build();
    }

    @GetMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> getAppointmentById(@NotNull @PathVariable("appointmentId") Long appointmentId) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.getAppointmentById(appointmentId))
                .build();
    }

    @GetMapping("present")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentPresent() throws JsonProcessingException, ParseException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentByAppointmentDate(new Date()))
                .build();
    }

    @PostMapping
    public APIResponse<AppointmentResponse> createAppointment(@NotNull @RequestBody AppointmentCreateRequest appointmentCreateRequest, @RequestParam(value = "emailNotification") boolean emailNotification) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.createAppointment(appointmentCreateRequest, emailNotification))
                .build();
    }

    @PutMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> updateAppointment(@NotNull @PathVariable("appointmentId") Long appointmentId, @NotNull @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        System.out.println(appointmentUpdateRequest);
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
