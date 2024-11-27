package com.pet_care.report_service.service;

import com.pet_care.report_service.service.impl.SaleQueueSourceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Service;
import org.apache.flink.streaming.api.datastream.DataStream;

import javax.jms.Message;
import javax.jms.TextMessage;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Makes all fields private by default (for better encapsulation)
public class FlinkJobService {
    StreamExecutionEnvironment env;

    JdbcConnectionOptions connectionOptions;

    @PostConstruct
    public void startFlinkJob() throws Exception {
        DataStream<String> messageStream = env.addSource(
                new SaleQueueSourceImpl()
        );
                // Sink to MySQL
        messageStream.addSink(JdbcSink.sink(
                "INSERT INTO sale_reports (name, created_at) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE created_at = VALUES(created_at)",
                (ps, event) -> {
                    ps.setString(1, (String) event);
                    ps.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                },
                connectionOptions
        ));

        // Execute Flink job
        env.execute("Flink with ActiveMQ and MySQL");
    }

    @PreDestroy
    public void stopFlinkJob() {
        // Lệnh dừng Flink Job (nếu cần)
        System.out.println("Stopping Flink job...");
        // Flink Job tự dừng khi môi trường Flink bị ngừng
    }

    private String extractMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                return ((TextMessage) message).getText();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
