package com.pet_care.notification_service.service;

// Import necessary annotations and classes for JSON processing, JMS, and HTTP client
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.notification_service.shared.utils.DateUtil;
import com.pet_care.notification_service.dto.response.AppointmentResponse;
import com.pet_care.notification_service.entity.AppointmentSendingEmailFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Queue;

@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates a constructor with required fields (final fields)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets all fields to private and final by default
public class BrevoService { // Service to handle notifications through the Brevo API

    RestClient restClient; // RestClient instance to send HTTP requests to the Brevo API

    ObjectMapper objectMapper;

    Queue<AppointmentResponse> upcomingAppointmentQueue;
    /**
     * Listens for messages from the "appointment-success-notification-queue" queue and sends a successful appointment email.
     *
     * @param message the message content received from the queue
     * @throws JsonProcessingException if there is an error processing JSON data
     */
    @JmsListener(destination = "appointment-success-notification-queue") // Configures this method as a listener for the specified queue
    public void sendAppointmentSuccessfulEmail(String message) throws JsonProcessingException {


        AppointmentResponse appointmentResponse = objectMapper.readValue(message, AppointmentResponse.class);

        System.out.println(appointmentResponse);

        AppointmentSendingEmailFormat emailBookingSuccessful = AppointmentSendingEmailFormat.builder()
                .appointmentId(appointmentResponse.getId())
                .appointmentDate(DateUtil.getDateOnly(appointmentResponse.getAppointmentDate()))
                .subject("Appointment Booking Successful")
                .appointmentTime(DateUtil.getTimeOnly(appointmentResponse.getAppointmentTime()))
                .toEmail(appointmentResponse.getEmail())
                .firstName(appointmentResponse.getFirstName())
                .lastName(appointmentResponse.getLastName())
                .templateId(1)
                .build();

        restClient.post() // Initiates a POST request
                .body(emailBookingSuccessful.getContent()) // Sets the message as the request body
                .retrieve(); // Executes the request and retrieves the response
    }

    @JmsListener(destination = "upcoming-appointment-queue") // Configures this method as a listener for the specified queue
    public void sendNotificationAppointmentUpcomingEmail(String message) throws JsonProcessingException {
        upcomingAppointmentQueue.add(objectMapper.readValue(message, AppointmentResponse.class));
    }
}
