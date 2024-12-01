package com.pet_care.appointment_service.service.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.mapper.AppointmentMapper;
import com.pet_care.appointment_service.repository.AppointmentRepository;
import com.pet_care.appointment_service.service.MessageBrokerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppointmentTask {

    AppointmentRepository appointmentRepository;

    ObjectMapper objectMapper;

    MessageBrokerService messageBrokerService;

    AppointmentMapper appointmentMapper;

    // This method will run at midnight every day
    @Scheduled(cron = "0 0 0 * * *")
    public void UpdateNoShowStatusAppointment() {
        appointmentRepository.updateNoShowStatusAppointment();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void NotificationAppointmentUpcoming() {
        appointmentRepository.getAppointmentsUpcoming().forEach(appointment -> {
            try {
                messageBrokerService.sendEvent("upcoming-appointment-queue",
                        objectMapper.writeValueAsString(
                            appointmentMapper.toDto(appointment)
                        )
                );
                Thread.sleep(100);
            } catch (JsonProcessingException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
