package com.pet_care.notification_service.service.task;

import com.pet_care.notification_service.common.utils.DateUtil;
import com.pet_care.notification_service.dto.response.AppointmentResponse;
import com.pet_care.notification_service.entity.AppointmentSendingEmailFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Queue;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendEmailTask {

    RestClient restClient;

    Queue<AppointmentResponse> notificationAppointmentQueue;

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Ho_Chi_Minh")
    public void notificationAppointment() {
        notificationAppointmentQueue.forEach(
                appointmentResponse -> {
                    AppointmentSendingEmailFormat emailNotification = AppointmentSendingEmailFormat.builder()
                            .appointmentId(appointmentResponse.getId())
                            .appointmentDate(DateUtil.getDateOnly(appointmentResponse.getAppointmentDate()))
                            .appointmentTime(DateUtil.getTimeOnly(appointmentResponse.getAppointmentTime()))
                            .toEmail(appointmentResponse.getEmail())
                            .firstName(appointmentResponse.getFirstName())
                            .lastName(appointmentResponse.getLastName())
                            .templateId(2)
                            .build();

                    restClient.post().body(emailNotification.getContent()).retrieve();
                }
        );

        notificationAppointmentQueue.clear();
    }
}
