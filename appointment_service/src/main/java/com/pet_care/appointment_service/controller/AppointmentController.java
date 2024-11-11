package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pet_care.appointment_service.dto.request.AppointmentCreateRequest;
import com.pet_care.appointment_service.dto.request.AppointmentUpdateRequest;
import com.pet_care.appointment_service.dto.response.APIResponse;
import com.pet_care.appointment_service.dto.response.AppointmentResponse;
import com.pet_care.appointment_service.dto.response.PageableResponse;
import com.pet_care.appointment_service.service.AppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("appointment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {

     AppointmentService appointmentService;

    /**
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping
    public APIResponse<List<AppointmentResponse>> getAllAppointment() throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointment())
                .build();
    }

    /**
     * @param startDate
     * @param endDate
     * @param statues
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<AppointmentResponse>> getAllAppointmentByStartDateAndEndDate(
            @RequestParam(value = "page",required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "50") int size,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "statues", required = false) Set<String> statues) throws JsonProcessingException {

        return APIResponse.<PageableResponse<AppointmentResponse>>builder()
                .data(appointmentService.filterAppointments(page, size, Objects.requireNonNullElse(startDate, LocalDate.now()), Objects.requireNonNullElse(endDate, LocalDate.now()), statues))
                .build();
    }

    /**
     * @param appointmentId
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> getAppointmentById( @PathVariable("appointmentId") Long appointmentId) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.getAppointmentById(appointmentId))
                .build();
    }

    /**
     * @return
     * @throws JsonProcessingException
     * @throws ParseException
     */
    @GetMapping("present")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentPresent() throws JsonProcessingException, ParseException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentByAppointmentDate(new Date()))
                .build();
    }

    /**
     * @param appointmentCreateRequest
     * @param emailNotification
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping
    public APIResponse<AppointmentResponse> createAppointment( @RequestBody AppointmentCreateRequest appointmentCreateRequest, @RequestParam(value = "emailNotification") boolean emailNotification) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.createAppointment(appointmentCreateRequest, emailNotification))
                .build();
    }

    /**
     * @param appointmentId
     * @param appointmentUpdateRequest
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> updateAppointment( @PathVariable("appointmentId") Long appointmentId,  @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        System.out.println(appointmentUpdateRequest);
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.updateAppointment(appointmentId, appointmentUpdateRequest))
                .build();
    }

    /**
     * @param appointmentId
     * @return
     */
    @PostMapping("/check-in/{appointmentId}")
    public APIResponse<Integer> checkInAppointment(@PathVariable("appointmentId") Long appointmentId) {

        return APIResponse.<Integer>builder()
                .data(appointmentService.checkInAppointment(appointmentId))
                .build();
    }

    /**
     * @param appointmentId
     * @return
     */
    @PostMapping("/cancel/{appointmentId}")
    public APIResponse<Integer> cancelAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.cancelAppointment(appointmentId))
                .build();
    }

    /**
     * @param appointmentId
     * @return
     */
    @PostMapping("/approved/{appointmentId}")
    public APIResponse<Integer> approvedAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.approvedAppointment(appointmentId))
                .build();
    }

    /**
     * @param accountId
     * @param status
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/account/{accountId}")
    public APIResponse<List<AppointmentResponse>> getAppointmentsByStatusAndAccountId(@PathVariable("accountId") Long accountId, @RequestParam("status") String status) throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatusAndAccountId(status, accountId))
                .build();
    }

    /**
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public APIResponse<List<AppointmentResponse>> getByStatus(@PathVariable("status") String status) {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatus(status))
                .build();
    }

    /**
     * @param appointmentId
     * @param services
     * @return
     * @throws JsonProcessingException
     */
    @PutMapping("/{appointmentId}/service")
    public APIResponse<AppointmentResponse> updateAppointmentService(@PathVariable("appointmentId") Long appointmentId, @RequestBody Set<String> services) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.updateAppointmentServices(appointmentId, services))
                .build();
    }

    /**
     * @param appointmentId
     * @return
     */
    @GetMapping("/isCheckin/{appointmentId}")
    public APIResponse<?> getAppointment(@PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.builder()
                .data(Map.of("isCheckIn:", appointmentService.checkInAppointment(appointmentId) == 1))
                .build();
    }


    /**
     * @return
     */
    @GetMapping("/up-coming")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentUpComing() {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentUpComing())
                .build();
    }
}
