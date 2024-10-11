package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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


/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketHandler extends TextWebSocketHandler {

    Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @NotNull JmsTemplate jmsTemplate;

    @NotNull ObjectMapper objectMapper;

    @NotNull
    @NonFinal
    Iterator<WebSocketSession> sessionIterator = sessions.iterator();


    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
        // Optional: handle incoming messages if needed
        super.handleMessage(session, message);
    }

    /**
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        sessions.add(session);
        // Reset iterator when a new session is added
        sessionIterator = sessions.iterator();
    }

    /**
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        sessions.remove(session);
        // Reset iterator if the current session was the iterator's next
        if (!sessionIterator.hasNext()) {
            sessionIterator = sessions.iterator();
        }
    }

    /**
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) throws Exception {
        // Handle error if needed
    }

    /**
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * @param message
     * @throws IOException
     */
    public void sendMessageRoundRobin(@NotNull String message) throws IOException {
        synchronized (sessions) {
            if (!sessions.isEmpty()) {
                if (!sessionIterator.hasNext()) {
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