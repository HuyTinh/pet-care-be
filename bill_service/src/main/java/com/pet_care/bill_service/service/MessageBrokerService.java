package com.pet_care.bill_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service // Marks this class as a service component
@RequiredArgsConstructor // Lombok annotation to generate the constructor with required fields
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Lombok annotation to set fields to private and final
public class MessageBrokerService {

    // Dependencies injected into the service
    JmsTemplate jmsTemplate; // JMS template for sending messages to queues
    ObjectMapper objectMapper; // Jackson ObjectMapper for serializing/deserializing objects
    InvoiceService invoiceService;

    @JmsListener(destination = "approved-bill-queue", containerFactory = "queueFactory") // JMS listener for receiving messages
    public void receiveMessageApprovedBill(String orderId) {
        invoiceService.approvedInvoice(Long.parseLong(orderId));
        try {
            Thread.sleep(1000); // Simulate a delay for processing the message (this can be optimized)
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle interruption or errors during sleep
        }
    }

    @JmsListener(destination = "cancelled-bill-queue") // JMS listener for receiving messages
    public void receiveMessageCancelledBill(String orderId) {
        invoiceService.canceledInvoice(Long.parseLong(orderId));
        try {
            Thread.sleep(1000); // Simulate a delay for processing the message (this can be optimized)
        } catch (Exception e) {
            throw new RuntimeException(e); // Handle interruption or errors during sleep
        }
    }

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
