package com.pet_care.notification_service.config;

// Import necessary classes for JMS connection configuration
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indicates that this class provides Spring configuration for beans
public class ActiveMQConfig {

    /**
     * Configures and provides a ConnectionFactory bean to connect to ActiveMQ.
     *
     * @return a configured ConnectionFactory instance for ActiveMQ
     */
    @Bean // Defines this method as a bean provider for dependency injection
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // Sets the broker URL for ActiveMQ
        factory.setUserName("admin"); // Sets the username for ActiveMQ authentication
        factory.setPassword("admin"); // Sets the password for ActiveMQ authentication
        return factory; // Returns the configured ConnectionFactory instance
    }
}
