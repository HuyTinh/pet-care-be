package com.pet_care.customer_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class for handling message sending via JMS (Java Message Service).
 * It provides functionality to send messages to the queue.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    // JMS Template for sending messages
    JmsTemplate jmsTemplate;

    // ObjectMapper to process JSON data
    ObjectMapper objectMapper;

    /**
     * Sends a message to the specified JMS queue.
     *
     * @param destination the destination queue to send the message
     * @param appointment the message content, typically an appointment object in JSON format
     */
    public void sendMessageQueue(String destination, String appointment) {
        jmsTemplate.convertAndSend(destination, appointment);
    }
}
