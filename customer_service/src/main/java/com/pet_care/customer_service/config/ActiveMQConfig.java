package com.pet_care.customer_service.config;

// Import necessary classes for ActiveMQ and Spring configuration
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Mark this class as a Spring configuration class
@Configuration
public class ActiveMQConfig {

    /**
     * Configures and returns a JMS ConnectionFactory for ActiveMQ.
     *
     * @return ConnectionFactory instance configured for ActiveMQ
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        // Create a new ActiveMQConnectionFactory with the specified broker URL
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Set credentials for connecting to the ActiveMQ broker
        factory.setUserName("admin");
        factory.setPassword("admin");

        // Return the configured factory
        return factory;
    }
}
