package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Iterator<WebSocketSession> sessionIterator = sessions.iterator();


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Optional: handle incoming messages if needed
        super.handleMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // Reset iterator when a new session is added
        sessionIterator = sessions.iterator();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        // Reset iterator if the current session was the iterator's next
        if (sessionIterator != null && !sessionIterator.hasNext()) {
            sessionIterator = sessions.iterator();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle error if needed
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendMessageRoundRobin(String message) throws IOException {
        synchronized (sessions) {
            if (!sessions.isEmpty()) {
                if (sessionIterator == null || !sessionIterator.hasNext()) {
                    sessionIterator = sessions.iterator(); // Reset iterator if needed
                }
                WebSocketSession session = null;
                if (sessionIterator.hasNext()) {
                    session = sessionIterator.next();
                }
                assert session != null;
                if (session.isOpen()) {
//                        TextMessage textMessage = (TextMessage) message;
                        session.sendMessage(new org.springframework.web.socket.TextMessage(message));

                }

                // Move iterator to the next client
                if (!sessionIterator.hasNext()) {
                    sessionIterator = sessions.iterator(); // Reset iterator if needed
                }
            } else {
//                if (message instanceof TextMessage) {
//                    TextMessage textMessage = (TextMessage) message;
//                    jmsTemplate.convertAndSend("petQueue", textMessage.getText());
//                }
            }
        }
    }
}