package com.pet_care.appointment_service.service;

import com.pet_care.appointment_service.enums.AppointmentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToAllUpdateListAppointment(String message, AppointmentStatus status) {
        Map<String, String> body = Map.of("appointmentId", message, "status", status.toString());
        messagingTemplate.convertAndSend("/topic/updateAppointment",  body);
    }

    public void sendToAllCreateAppointment(String message) {
        messagingTemplate.convertAndSend("/topic/createAppointment", message);
    }

    public void sendToExportPDFAppointment(String sessionId, String message) {
        messagingTemplate.convertAndSend("/topic/exportPDF/" + sessionId, message);
    }
}
