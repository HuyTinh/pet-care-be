package com.pet_care.appointment_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import com.pet_care.appointment_service.service.AppointmentService;
import com.pet_care.appointment_service.service.MessageService;
import com.pet_care.appointment_service.service.WebSocketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 *
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketController {

    @NotNull WebSocketService webSocketService;

    @NotNull SimpMessagingTemplate messagingTemplate;

    @NotNull ObjectMapper objectMapper;

    @NotNull AppointmentService appointmentService;

    @NotNull MessageService messageService;

    /**
     * @param message
     * @throws Exception
     */
    @Transactional
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload @NotNull Map<String, String> message) throws Exception {
        // Xử lý tin nhắn tại đây

        long appointmentId = Long.parseLong(message.get("appointmentId"));
        String sessionId = message.get("sessionId");
        AppointmentStatus status = AppointmentStatus.valueOf(message.get("status"));
        switch (status) {
            case CHECKED_IN -> {
                if (appointmentService.checkInAppointment((appointmentId)) > 0) {
                    webSocketService.sendToAllUpdateListAppointment(Long.toString(appointmentId), status);
                    webSocketService.sendToExportPDFAppointment(sessionId, Long.toString(appointmentId));
                    messageService.sendMessage("doctor-appointment-queue", objectMapper.writeValueAsString(appointmentService.getAppointmentById(appointmentId)));
                }
            }
            case CANCELLED -> {
                if (appointmentService.cancelAppointment((appointmentId)) > 0) {
                    webSocketService.sendToAllUpdateListAppointment(Long.toString(appointmentId), status);
                }
            }
        }
    }
}
