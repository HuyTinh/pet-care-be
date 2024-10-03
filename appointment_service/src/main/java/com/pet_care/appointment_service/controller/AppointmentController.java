package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.ApiResponse;
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
    public ApiResponse<List<AppointmentResponse>> getAll() throws JsonProcessingException {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAll())
                .build();
    }

    @GetMapping("present")
    public ApiResponse<List<AppointmentResponse>> getAppointmentPresent(@RequestParam("statuses") Set<AppointmentStatus> statuses) throws JsonProcessingException, ParseException {

        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getAppointmentByAppointmentDateAndAndStatusIn(new Date(), statuses))
                .build();
    }

//    @GetMapping("/account/{accountId}")
//    public ApiResponse<List<AppointmentResponse>> getAllByAccountId(@PathVariable("accountId") Long accountId) throws JsonProcessingException {
//        return ApiResponse.<List<AppointmentResponse>>builder()
//                .result(appointmentService.getByAccountId(accountId))
//                .build();
//    }

    @PostMapping
    public ApiResponse<AppointmentResponse> create(@RequestBody AppointmentCreateRequest appointmentCreateRequest) throws JsonProcessingException {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.createNoneEmailNotification(appointmentCreateRequest))
                .build();
    }

    @PutMapping("{appointmentId}")
    public ApiResponse<AppointmentResponse> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        return ApiResponse.<AppointmentResponse>builder()
                .result(appointmentService.updateAppointment(appointmentId, appointmentUpdateRequest))
                .build();
    }

    @PostMapping("/approved/{appointmentId}")
    public ApiResponse<Integer> checkInAppointment(@PathVariable Long appointmentId) {
        return ApiResponse.<Integer>builder()
                .result(appointmentService.checkInAppointment(appointmentId))
                .build();
    }

    @PostMapping("/cancel/{appointmentId}")
    public ApiResponse<Integer> cancelAppointment(@PathVariable Long appointmentId) {
        return ApiResponse.<Integer>builder()
                .result(appointmentService.cancelAppointment(appointmentId))
                .build();
    }

    @GetMapping("/account/{accountId}")
    public ApiResponse<List<AppointmentResponse>> getAppointmentsByStatus(@PathVariable("accountId") Long accountId, @RequestParam("status") String status) throws JsonProcessingException {
        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getByStatusAndAccountId(status, accountId))
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<AppointmentResponse>> getByStatusAndCustomerId(@PathVariable("status") String status) {

        return ApiResponse.<List<AppointmentResponse>>builder()
                .result(appointmentService.getByStatus(status))
                .build();
    }

    @GetMapping("/isCheckin/{appointmentId}")
    public ApiResponse<?> getAppointment(@PathVariable Long appointmentId) {
        return ApiResponse.builder()
                .result(Map.of("isCheckIn:", appointmentService.checkInAppointment(appointmentId) == 1))
                .build();

    }
}
