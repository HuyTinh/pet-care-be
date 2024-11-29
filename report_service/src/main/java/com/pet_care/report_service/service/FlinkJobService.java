package com.pet_care.report_service.service;

import com.pet_care.report_service.common.shared.ActiveMQSource;
import com.pet_care.report_service.service.sink.PrescriptionSaleReportsSink;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.sql.Connection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Makes all fields private by default (for better encapsulation)
public class FlinkJobService {

    StreamExecutionEnvironment env;

    JdbcConnectionOptions jdbcOptions;

    Connection connection;

    @PostConstruct
    public void startFlinkJob() throws Exception {
        DataStream<String> messageStream = env.addSource(
                new ActiveMQSource("tcp://localhost:61616", "sale-report-queue")
        ).map(value -> {
            // Xử lý dữ liệu tại đây
            System.out.println(value);
            return "Processed: " + value;
        });

        messageStream.addSink(new PrescriptionSaleReportsSink());
        // Thực thi job
        env.execute("Flink with ActiveMQ and MySQL");
    }

    @PreDestroy
    public void stopFlinkJob() {
        // Lệnh dừng Flink Job (nếu cần)
        System.out.println("Stopping Flink job...");
        // Flink Job tự dừng khi môi trường Flink bị ngừng
    }
}
