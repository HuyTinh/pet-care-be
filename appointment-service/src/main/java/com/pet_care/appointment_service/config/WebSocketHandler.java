package com.pet_care.appointment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
 * WebSocketHandler class handles WebSocket session management and message sending in a round-robin manner.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketHandler extends TextWebSocketHandler {

    // A thread-safe set to store WebSocket sessions
    Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    // JMS template for sending messages, if needed
    JmsTemplate jmsTemplate;

    // ObjectMapper for parsing or sending messages, if necessary
    ObjectMapper objectMapper;

    // Iterator to track sessions for round-robin message sending
    @NonFinal
    Iterator<WebSocketSession> sessionIterator = sessions.iterator();

    /**
     * Handles incoming WebSocket messages. This can be extended to process messages if needed.
     *
     * @param session The WebSocket session.
     * @param message The WebSocket message.
     * @throws Exception In case of any error during message handling.
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Optional: handle incoming messages if needed
        super.handleMessage(session, message);
    }

    /**
     * Adds the session to the set of sessions when a new WebSocket connection is established.
     * Resets the iterator to include the new session.
     *
     * @param session The WebSocket session.
     * @throws Exception In case of any error during session establishment.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // Reset iterator when a new session is added
        sessionIterator = sessions.iterator();
    }

    /**
     * Removes the session from the set of sessions when the WebSocket connection is closed.
     * Resets the iterator if the current session was the last in the iteration.
     *
     * @param session The WebSocket session.
     * @param status The close status of the connection.
     * @throws Exception In case of any error during session closure.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        // Reset iterator if the current session was the iterator's next
        if (!sessionIterator.hasNext()) {
            sessionIterator = sessions.iterator();
        }
    }

    /**
     * Handles transport errors in WebSocket communication. This method can be extended to log or manage errors.
     *
     * @param session The WebSocket session.
     * @param exception The exception encountered during transport.
     * @throws Exception In case of any error during transport.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle error if needed
    }

    /**
     * Returns whether partial messages are supported. This is set to false for full message handling.
     *
     * @return False since partial messages are not supported.
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * Sends a message to the next WebSocket session in a round-robin manner. If there are no open sessions, the message is not sent.
     *
     * @param message The message to send.
     * @throws IOException If there is an error during message sending.
     */
    public void sendMessageRoundRobin(String message) throws IOException {
        synchronized (sessions) {
            if (!sessions.isEmpty()) {
                // Reset iterator if necessary
                if (!sessionIterator.hasNext()) {
                    sessionIterator = sessions.iterator();
                }
                WebSocketSession session = null;
                if (sessionIterator.hasNext()) {
                    session = sessionIterator.next();
                }
                assert session != null;
                // Check if the session is open before sending the message
                if (session.isOpen()) {
                    // Send the message to the session
                    session.sendMessage(new org.springframework.web.socket.TextMessage(message));
                }

                // Move iterator to the next client
                if (!sessionIterator.hasNext()) {
                    sessionIterator = sessions.iterator();
                }
            } else {
                // Optionally, send the message to a JMS queue if there are no open WebSocket sessions
                // jmsTemplate.convertAndSend("petQueue", message);
            }
        }
    }
}
