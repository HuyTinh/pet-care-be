package com.pet_care.payment_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service // Marks this class as a service component
@RequiredArgsConstructor // Lombok annotation to generate the constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields to private and final
public class MessageBrokerService {

    JmsTemplate jmsTemplate;

    /**
     * Sends a message to a specified destination queue.
     * @param destination The destination queue to send the message to.
     * @param object The message to send.
     */
    public void sendEvent(String destination, String object) {
        // Send the appointment message to the specified queue using JMS template
        jmsTemplate.convertAndSend(destination, object);
    }
}
