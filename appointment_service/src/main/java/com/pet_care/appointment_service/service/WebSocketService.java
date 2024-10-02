package com.pet_care.appointment_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToAllUpdateListAppointment(String message) {
        messagingTemplate.convertAndSend("/topic/updateAppointment", message);
    }

    public void sendToAllCreateAppointment(String message) {
        messagingTemplate.convertAndSend("/topic/createAppointment", message);
    }

    public void sendToExportPDFAppointment(String sessionId, String message) {
        messagingTemplate.convertAndSend("/topic/exportPDF/" + sessionId, message);
    }
}
