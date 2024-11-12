package com.pet_care.appointment_service.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for setting up WebSocket support with STOMP messaging.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for handling WebSocket messages.
     *
     * @param config The MessageBrokerRegistry to configure the broker.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables a simple in-memory broker for messages prefixed with "/topic".
        config.enableSimpleBroker("/topic");
        // Sets the prefix for application-bound messages (i.e., messages sent from clients).
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers the STOMP endpoints for WebSocket communication.
     *
     * @param registry The StompEndpointRegistry to register STOMP endpoints.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers a WebSocket endpoint at "/ws" and allows connections from specified origins.
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173/", "https://tsm885rc-5173.asse.devtunnels.ms/")
                .withSockJS(); // Enables SockJS fallback options for browsers that do not support WebSockets.
    }

    /**
     * Defines a WebSocket handler bean for handling WebSocket connections.
     *
     * @return A new instance of WebSocketHandler.
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }
}
