package com.pet_care.appointment_service.config;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Configuration class for ActiveMQ setup and JMS listener container factories.
 */
@Configuration
public class ActiveMQConfig {

    /**
     * Bean for a Queue to store messages.
     *
     * @return A new ArrayDeque to serve as a Queue.
     */
    @Bean
    Queue<String> queue() {
        return new ArrayDeque<>();
    }


    /**
     * Bean to configure the JMS listener container factory for topics.
     *
     * @param connectionFactory The connection factory to be used by the listener container.
     * @return A configured DefaultJmsListenerContainerFactory for topics.
     */
    @Bean
    public DefaultJmsListenerContainerFactory topicFactory(@NotNull ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSubscriptionDurable(true); // Enable durable subscriptions
        factory.setClientId("durableClientId"); // Set the client ID for durable subscriptions
        factory.setPubSubDomain(true); // Set to true for topics (Publish-Subscribe mode)
        return factory;
    }

    /**
     * Bean to configure the JMS listener container factory for queues.
     *
     * @param connectionFactory The connection factory to be used by the listener container.
     * @return A configured DefaultJmsListenerContainerFactory for queues.
     */
    @Bean
    public DefaultJmsListenerContainerFactory queueFactory(@NotNull ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
