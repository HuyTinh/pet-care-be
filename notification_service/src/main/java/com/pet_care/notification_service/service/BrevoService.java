package com.pet_care.notification_service.service;

// Import necessary annotations and classes for JSON processing, JMS, and HTTP client
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service // Marks this class as a Spring service component
@RequiredArgsConstructor // Generates a constructor with required fields (final fields)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Sets all fields to private and final by default
public class BrevoService { // Service to handle notifications through the Brevo API

    RestClient restClient; // RestClient instance to send HTTP requests to the Brevo API

    /**
     * Listens for messages from the "appointment-success-notification-queue" queue and sends a successful appointment email.
     *
     * @param message the message content received from the queue
     * @throws JsonProcessingException if there is an error processing JSON data
     */
    @JmsListener(destination = "appointment-success-notification-queue") // Configures this method as a listener for the specified queue
    public void sendAppointmentSuccessfulEmail(String message) throws JsonProcessingException {
        restClient.post() // Initiates a POST request
                .body(message) // Sets the message as the request body
                .retrieve(); // Executes the request and retrieves the response
    }
}
