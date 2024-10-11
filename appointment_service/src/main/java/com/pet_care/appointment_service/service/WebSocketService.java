package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.enums.AppointmentStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketService {

    SimpMessagingTemplate messagingTemplate;

    /**
     * @param message
     * @param status
     */
    public void sendToAllUpdateListAppointment(@NotNull String message, @NotNull AppointmentStatus status) {
        Map<String, String> body = Map.of("appointmentId", message, "status", status.toString());
        messagingTemplate.convertAndSend("/topic/updateAppointment", body);
    }

    /**
     * @param message
     */
    public void sendToAllCreateAppointment(@NotNull String message) {
        messagingTemplate.convertAndSend("/topic/createAppointment", message);
    }

    /**
     * @param sessionId
     * @param message
     */
    public void sendToExportPDFAppointment(String sessionId, @NotNull String message) {
        messagingTemplate.convertAndSend("/topic/exportPDF/" + sessionId, message);
    }
}
