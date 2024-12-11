package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.service.AppointmentService;
import com.pet_care.appointment_service.service.MessageBrokerService;
import com.pet_care.appointment_service.service.WebSocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * WebSocketController handles WebSocket communication for appointment-related events.
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketController {

    // Service for WebSocket communication
    WebSocketService webSocketService;

    // Used for sending WebSocket messages
    SimpMessagingTemplate messagingTemplate;

    // ObjectMapper to handle JSON serialization
    ObjectMapper objectMapper;

    // Service for appointment-related logic
    AppointmentService appointmentService;

    // Service for handling message sending
    MessageBrokerService messageBrokerService;

    /**
     * Handles incoming WebSocket messages to update the status of appointments.
     * Depending on the status, it will trigger appropriate actions (e.g., check-in or cancellation).
     *
     * @param message The incoming WebSocket message containing appointment details.
     * @throws Exception If an error occurs during processing.
     */
    @Transactional
    @MessageMapping("/sendEvent")
    public void sendMessage(@Payload Map<String, String> message) throws Exception {
        // Extracts necessary information from the message
        long appointmentId = Long.parseLong(message.get("appointmentId"));
        String sessionId = message.get("sessionId");
        AppointmentStatus status = AppointmentStatus.valueOf(message.get("status"));

        switch (status) {
            case CHECKED_IN:
                // Process the "checked-in" status
                if (appointmentService.checkInAppointment(appointmentId) > 0) {
                    // Sends updates to all connected clients about the appointment status change
                    webSocketService.sendToAllUpdateListAppointment(Long.toString(appointmentId), status);
                    // Triggers the export of the appointment details to a PDF
                    webSocketService.sendToExportPDFAppointment(sessionId, Long.toString(appointmentId));
                    // Sends the appointment data to a messaging queue for the doctor appointment system
                    messageBrokerService.sendEvent("doctor-appointment-queue", objectMapper.writeValueAsString(appointmentService.getAppointmentById(appointmentId)));
                }
                break;
            case CANCELLED:
                // Process the "cancelled" status
                if (appointmentService.cancelAppointment(appointmentId) > 0) {
                    // Sends updates to all connected clients about the appointment status change
                    webSocketService.sendToAllUpdateListAppointment(Long.toString(appointmentId), status);
                }
                break;
        }
    }
}
