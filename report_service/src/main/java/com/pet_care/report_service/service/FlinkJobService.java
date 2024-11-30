package com.pet_care.report_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.report_service.common.shared.ActiveMQSource;
import com.pet_care.report_service.dto.request.ReportCreateRequest;
import com.pet_care.report_service.enums.ReportOf;
import com.pet_care.report_service.service.sink.PrescriptionSaleReportsSink;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Service;
import org.apache.flink.streaming.api.datastream.DataStream;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Makes all fields private by default (for better encapsulation)
public class FlinkJobService {

    StreamExecutionEnvironment env;

    JdbcConnectionOptions jdbcOptions;

    ObjectMapper objectMapper;

    @PostConstruct
    public void startFlinkJob() throws Exception {
        DataStream<ReportCreateRequest> messageStream = env.addSource(
                new ActiveMQSource("tcp://localhost:61616", "report-service-queue")
        ).map(value -> objectMapper.readValue(value, ReportCreateRequest.class));

        DataStream<String> prescriptionStream = messageStream.filter(request ->
                ReportOf.valueOf(request.getOf()) == ReportOf.PRESCRIPTION).map(ReportCreateRequest::getData);

        DataStream<String> appointmentStream = messageStream.filter(request ->
                ReportOf.valueOf(request.getOf()) == ReportOf.APPOINTMENT).map(ReportCreateRequest::getData);

        prescriptionStream.addSink(new PrescriptionSaleReportsSink());
//        appointmentStream.addSink(new PrescriptionSaleReportsSink());
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
