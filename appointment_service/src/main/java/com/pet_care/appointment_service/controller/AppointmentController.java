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

/**
 * AppointmentController handles all the HTTP requests related to appointments, including
 * creating, updating, fetching, and managing appointments.
 */
@RestController
@RequestMapping("appointment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {

    // The AppointmentService is used to handle the business logic related to appointments
    AppointmentService appointmentService;

    /**
     * Retrieves all appointments from the system.
     *
     * @return A response containing the list of all appointments.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @GetMapping
    public APIResponse<List<AppointmentResponse>> getAllAppointment() throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointment())
                .build();
    }

    /**
     * Retrieves appointments based on filter parameters like date range and status.
     *
     * @param page The page number to fetch (default is 0).
     * @param size The number of appointments per page (default is 50).
     * @param startDate The start date for the appointment filter.
     * @param endDate The end date for the appointment filter.
     * @param statues The set of appointment statuses to filter by.
     * @return A paginated response of filtered appointments.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @GetMapping("/filter")
    public APIResponse<PageableResponse<AppointmentResponse>> getAllAppointmentByStartDateAndEndDate(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "50") int size,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "statues", required = false) Set<String> statues) throws JsonProcessingException {

        return APIResponse.<PageableResponse<AppointmentResponse>>builder()
                .data(appointmentService.filterAppointments(page, size, Objects.requireNonNullElse(startDate, LocalDate.now()), Objects.requireNonNullElse(endDate, LocalDate.now()), statues))
                .build();
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return A response containing the appointment details.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @GetMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.getAppointmentById(appointmentId))
                .build();
    }

    /**
     * Retrieves appointments scheduled for the current day.
     *
     * @return A response containing the list of appointments for today.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     * @throws ParseException If there is an error parsing the date.
     */
    @GetMapping("present")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentPresent() throws JsonProcessingException, ParseException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentByAppointmentDate(new Date()))
                .build();
    }

    /**
     * Creates a new appointment and optionally sends an email notification.
     *
     * @param appointmentCreateRequest The request body containing appointment details.
     * @param emailNotification Whether to send an email notification.
     * @return A response containing the created appointment details.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @PostMapping
    public APIResponse<AppointmentResponse> createAppointment(@RequestBody AppointmentCreateRequest appointmentCreateRequest, @RequestParam(value = "emailNotification") boolean emailNotification) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.createAppointment(appointmentCreateRequest, emailNotification))
                .build();
    }

    /**
     * Updates an existing appointment.
     *
     * @param appointmentId The ID of the appointment to update.
     * @param appointmentUpdateRequest The request body containing updated appointment details.
     * @return A response containing the updated appointment details.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @PutMapping("/{appointmentId}")
    public APIResponse<AppointmentResponse> updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.updateAppointment(appointmentId, appointmentUpdateRequest))
                .build();
    }

    /**
     * Marks an appointment as checked in.
     *
     * @param appointmentId The ID of the appointment to check in.
     * @return A response indicating the success of the check-in operation.
     */
    @PostMapping("/check-in/{appointmentId}")
    public APIResponse<Integer> checkInAppointment(@PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.checkInAppointment(appointmentId))
                .build();
    }

    /**
     * Cancels an appointment.
     *
     * @param appointmentId The ID of the appointment to cancel.
     * @return A response indicating the success of the cancellation.
     */
    @PostMapping("/cancel/{appointmentId}")
    public APIResponse<Integer> cancelAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.cancelAppointment(appointmentId))
                .build();
    }

    /**
     * Approves an appointment.
     *
     * @param appointmentId The ID of the appointment to approve.
     * @return A response indicating the success of the approval.
     */
    @PostMapping("/approved/{appointmentId}")
    public APIResponse<Integer> approvedAppointment(@PathVariable Long appointmentId) {
        return APIResponse.<Integer>builder()
                .data(appointmentService.approvedAppointment(appointmentId))
                .build();
    }

    /**
     * Retrieves appointments by account ID and status.
     *
     * @param accountId The account ID to filter appointments by.
     * @param status The status of the appointments to filter.
     * @return A response containing the filtered list of appointments.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @GetMapping("/account/{accountId}")
    public APIResponse<List<AppointmentResponse>> getAppointmentsByStatusAndAccountId(@PathVariable("accountId") Long accountId, @RequestParam("status") String status) throws JsonProcessingException {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatusAndAccountId(status, accountId))
                .build();
    }

    /**
     * Retrieves appointments by status.
     *
     * @param status The status to filter appointments by.
     * @return A response containing the filtered list of appointments.
     */
    @GetMapping("/status/{status}")
    public APIResponse<List<AppointmentResponse>> getByStatus(@PathVariable("status") String status) {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getByStatus(status))
                .build();
    }

    /**
     * Updates the services associated with an appointment.
     *
     * @param appointmentId The ID of the appointment to update.
     * @param services The set of services to associate with the appointment.
     * @return A response containing the updated appointment details.
     * @throws JsonProcessingException If there is an error processing the data into JSON format.
     */
    @PutMapping("/{appointmentId}/service")
    public APIResponse<AppointmentResponse> updateAppointmentService(@PathVariable("appointmentId") Long appointmentId, @RequestBody Set<String> services) throws JsonProcessingException {
        return APIResponse.<AppointmentResponse>builder()
                .data(appointmentService.updateAppointmentServices(appointmentId, services))
                .build();
    }

    /**
     * Checks whether an appointment is checked in.
     *
     * @param appointmentId The ID of the appointment to check.
     * @return A response indicating whether the appointment is checked in.
     */
    @GetMapping("/isCheckin/{appointmentId}")
    public APIResponse<?> getAppointment(@PathVariable("appointmentId") Long appointmentId) {
        return APIResponse.builder()
                .data(Map.of("isCheckIn:", appointmentService.checkInAppointment(appointmentId) == 1))
                .build();
    }

    /**
     * Retrieves upcoming appointments.
     *
     * @return A response containing the list of upcoming appointments.
     */
    @GetMapping("/up-coming")
    public APIResponse<List<AppointmentResponse>> getAllAppointmentUpComing() {
        return APIResponse.<List<AppointmentResponse>>builder()
                .data(appointmentService.getAllAppointmentUpComing())
                .build();
    }
}
