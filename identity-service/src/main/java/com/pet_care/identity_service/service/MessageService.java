package com.pet_care.identity_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {
     JmsTemplate jmsTemplate;

     ObjectMapper objectMapper;

    /**
     * @param destination
     * @param customer
     * @throws JsonProcessingException
     */
    public void sendMessageQueue( String destination,  String customer) throws JsonProcessingException {
        jmsTemplate.convertAndSend(destination, customer);
    }
}
