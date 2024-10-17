package com.pet_care.message_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.message_service.entity.Pet;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;



public class TestSendRequest {
    private ObjectMapper objectMapper;

    public static void main(String[] args) throws JMSException, JsonProcessingException {
        try {
            // Tạo kết nối tới ActiveMQ
            ConnectionFactory factory = (ConnectionFactory) new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = factory.createConnection();
            connection.start();

            // Tạo session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Destination queue = session.createQueue("petQueue");

            // Tạo producer
            MessageProducer producer = session.createProducer(queue);



            ObjectMapper objectMapper = new ObjectMapper();
            // Tạo và gửi tin nhắn
            for (int i = 1; i <= 1000; i++) {
                TextMessage message = session.createTextMessage(objectMapper.writeValueAsString(new Pet((long) i, "Thú cưng ("+i+")")));
                producer.send(message);
                System.out.println("Sent: " + message.getText());
            }

            // Dọn dẹp
            producer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
