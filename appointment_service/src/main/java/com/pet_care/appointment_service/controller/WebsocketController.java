package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.service.AppointmentService;
import com.pet_care.appointment_service.service.MessageService;
import com.pet_care.appointment_service.service.WebSocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketController {

    WebSocketService webSocketService;

    SimpMessagingTemplate messagingTemplate;

    ObjectMapper objectMapper;

    AppointmentService appointmentService;

    MessageService messageService;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Map<String, String> message) throws Exception {
        // Xử lý tin nhắn tại đây
        long appointmentId = Long.parseLong(message.get("appointmentId"));
        String sessionId = message.get("sessionId");
        if (appointmentService.checkInAppointment((appointmentId)) > 0) {
            webSocketService.sendToAllUpdateListAppointment(Long.toString(appointmentId));
            webSocketService.sendToExportPDFAppointment(sessionId, Long.toString(appointmentId));
            messageService.sendMessage("doctor-appointment-queue", objectMapper.writeValueAsString(appointmentService.getById(Long.toString(appointmentId))));
        }
    }
}
