package com.pet_care.message_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.message_service.configuration.WebSocketHandler;
import com.pet_care.message_service.entity.Pet;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final WebSocketHandler webSocketHandler;

    private final JmsTemplate jmsTemplate;

    private final PriorityQueue<Pet> messages;

    private final ObjectMapper objectMapper;


    public void sendMessages() {
        for (int i = 1; i <= 100; i++) {
            String message = "Message " + i;
            jmsTemplate.convertAndSend("myQueue", message);
            System.out.println("Sent: " + message);
        }
    }

    @JmsListener(destination = "petQueue")
    public void receiveMessage(Message message) {
//        TextMessage textMessage = (TextMessage) message;
//        messages.add(objectMapper.readValue(textMessage.getText(), Pet.class));
        try {
            webSocketHandler.sendMessageRoundRobin(message);
        } catch (JMSException | IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
