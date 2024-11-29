package com.pet_care.report_service.common.shared;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class ActiveMQSource implements SourceFunction<String> {
    private volatile boolean isRunning = true;
    private final String brokerUrl;
    private final String queueName;

    public ActiveMQSource(String brokerUrl, String queueName) {
        this.brokerUrl = brokerUrl;
        this.queueName = queueName;
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        try (Connection connection = factory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);

            while (isRunning) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    ctx.collect(((TextMessage) message).getText());
                }
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}