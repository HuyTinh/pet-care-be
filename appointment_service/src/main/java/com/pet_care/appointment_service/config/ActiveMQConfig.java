package com.pet_care.appointment_service.config;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

@Configuration
public class ActiveMQConfig {
    @NotNull
    @Bean
    Queue<String> queue() {
        return new ArrayDeque<>();
    }

    @NotNull
    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setUserName("admin");
        factory.setPassword("admin");
        return factory;
    }

    @NotNull
    @Bean
    public DefaultJmsListenerContainerFactory topicFactory(@NotNull ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true); // Enable durable subscriptions
        factory.setClientId("durableClientId");
        factory.setPubSubDomain(true); // Set to true for topics
        return factory;
    }

    @NotNull
    @Bean
    public DefaultJmsListenerContainerFactory queueFactory(@NotNull ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
