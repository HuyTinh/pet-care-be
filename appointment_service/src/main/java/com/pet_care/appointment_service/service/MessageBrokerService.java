package com.pet_care.appointment_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.appointment_service.config.WebSocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service // Marks this class as a service component
@RequiredArgsConstructor // Lombok annotation to generate the constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields to private and final
public class MessageBrokerService {

    // Dependencies injected into the service
    Queue<String> petQueue; // A queue to hold incoming messages
    JmsTemplate jmsTemplate; // JMS template for sending messages to queues
    ObjectMapper objectMapper; // Jackson ObjectMapper for serializing/deserializing objects
    WebSocketHandler webSocketHandler; // Handler for managing WebSocket connections
    WebSocketService webSocketService; // Service to send messages over WebSocket

    /**
     * Receives a message from the "receptionist-appointment-queue" JMS queue.
     * Adds the message to the petQueue for processing.
     * @param message The message received from the queue.
     */
    @JmsListener(destination = "receptionist-appointment-queue", containerFactory = "queueFactory") // JMS listener for receiving messages
    public void receiveMessage(String message) {
        petQueue.add(message); // Add the message to the queue for later processing
        try {
            Thread.sleep(1000); // Simulate a delay for processing the message (this can be optimized)
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle interruption or errors during sleep
        }
    }

    /**
     * Sends a message to a specified destination queue.
     * @param destination The destination queue to send the message to.
     * @param appointment The message to send.
     */
    public void sendEvent(String destination, String appointment) {
        // Send the appointment message to the specified queue using JMS template
        jmsTemplate.convertAndSend(destination, appointment);
    }

    /**
     * Periodically checks the petQueue and sends the next appointment message to WebSocket clients.
     * This method runs every second.
     */
    @Scheduled(fixedRate = 1000) // This method is scheduled to run every 1000 ms (1 second)
    public void reportCurrentTime() {
        // If there are messages in the queue, send the next one
        if (!petQueue.isEmpty()) {
            try {
                // Send the next appointment to all connected WebSocket clients
                webSocketService.sendToAllCreateAppointment(petQueue.poll()); // Poll removes and retrieves the first element
            } catch (Exception ignored) {
                // Ignore any exceptions that occur during message sending (to avoid crashing the service)
            }
        }
    }
}
