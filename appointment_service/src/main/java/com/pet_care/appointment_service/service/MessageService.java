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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    Queue<String> petQueue;

    JmsTemplate jmsTemplate;

    ObjectMapper objectMapper;

    WebSocketHandler webSocketHandler;

    WebSocketService webSocketService;


    @JmsListener(destination = "receptionist-appointment-queue", containerFactory = "queueFactory")
    public void receiveMessage(String message) {
           petQueue.add(message);
           System.out.println("Add: " + message +" to queue");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }


    public void sendMessage(String destination ,String appointment) {
        jmsTemplate.convertAndSend(destination,appointment);
    }

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        if(!petQueue.isEmpty()){
//            System.out.println(petQueue.peek());
           try {
//               webSocketHandler.sendMessageRoundRobin(petQueue.poll());
               webSocketService.sendToAllCreateAppointment(petQueue.poll());
           } catch (Exception ignored) {}
        }
    }
}
