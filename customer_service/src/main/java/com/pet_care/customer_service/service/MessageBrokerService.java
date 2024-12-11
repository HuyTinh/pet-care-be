package com.pet_care.customer_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.customer_service.dto.request.CustomerCreateRequest;
import com.pet_care.customer_service.mapper.CustomerMapper;
import com.pet_care.customer_service.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling message sending via JMS (Java Message Service).
 * It provides functionality to send messages to the queue.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageBrokerService {

    // JMS Template for sending messages
    JmsTemplate jmsTemplate;

    // ObjectMapper to process JSON data
    ObjectMapper objectMapper;

    RedisNativeService redisNativeService;

    CustomerRepository customerRepository;

    CustomerMapper customerMapper;

    /**
     * Processes a message from the queue and adds a new customer to the repository.
     *
     * @param customerRequest the customer data received from the queue
     * @throws JsonProcessingException if there is an error processing JSON data
     */
    @JmsListener(destination = "customer-create-queue")
    @Transactional
    public void addCustomerEvent(String customerRequest) throws JsonProcessingException {
        customerRepository.save(customerMapper.toEntity(objectMapper.readValue(customerRequest, CustomerCreateRequest.class)));
        cacheCustomer();
    }

    /**
     * Sends a message to the specified JMS queue.
     *
     * @param destination the destination queue to send the message
     * @param appointment the message content, typically an appointment object in JSON format
     */
    public void sendEvent(String destination, String appointment) {
        jmsTemplate.convertAndSend(destination, appointment);
    }

    private void cacheCustomer() {
        redisNativeService.deleteRedisList("customer-response-list");
        redisNativeService.saveToRedisList("customer-response-list",
                customerRepository.findAll().parallelStream().map(customerMapper::toDto)
                        .toList(),3600);
    }
}
