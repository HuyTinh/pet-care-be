package com.pet_care.message_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.message_service.configuration.WebSocketHandler;
import com.pet_care.message_service.entity.Pet;
import jakarta.annotation.PostConstruct;
import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.PriorityQueue;

@SpringBootApplication
@RequiredArgsConstructor
public class MessageServiceApplication {

    private final ObjectMapper objectMapper;
    private final WebSocketHandler webSocketHandler;
    private final PriorityQueue<Pet> pets;

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }
}
