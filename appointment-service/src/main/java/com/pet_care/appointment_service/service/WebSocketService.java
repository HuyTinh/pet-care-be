package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.enums.AppointmentStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service // Marks this class as a service component to be managed by Spring
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields as private and final
public class WebSocketService {

    SimpMessagingTemplate messagingTemplate; // Used for sending messages over WebSocket

    /**
     * Sends an update to all clients about an appointment's status change.
     * @param message The appointment ID to be sent.
     * @param status The new status of the appointment.
     */
    public void sendToAllUpdateListAppointment(String message, AppointmentStatus status) {
        // Create a map containing appointment ID and status, then send it to the "/topic/updateAppointment" topic
        Map<String, String> body = Map.of("appointmentId", message, "status", status.toString());
        messagingTemplate.convertAndSend("/topic/updateAppointment", body);
    }

    /**
     * Sends a notification to all clients about the creation of a new appointment.
     * @param message The appointment ID or related message to be sent.
     */
    public void sendToAllCreateAppointment(String message) {
        // Send the appointment creation message to the "/topic/createAppointment" topic
        messagingTemplate.convertAndSend("/topic/createAppointment", message);
    }

    /**
     * Sends a message for exporting an appointment's details as a PDF.
     * @param sessionId The WebSocket session ID for targeting the specific client.
     * @param message The appointment-related data to be sent for PDF export.
     */
    public void sendToExportPDFAppointment(String sessionId, String message) {
        // Send the message to a specific client (identified by sessionId) for exporting the appointment to PDF
        messagingTemplate.convertAndSend("/topic/exportPDF/" + sessionId, message);
    }
}
