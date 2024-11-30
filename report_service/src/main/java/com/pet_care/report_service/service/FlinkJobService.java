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
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
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
        // Tạo tags cho side outputs
        final OutputTag<String> prescriptionTag = new OutputTag<>("prescription") {};
        final OutputTag<String> appointmentTag = new OutputTag<>("appointment") {};

        // Xử lý dữ liệu chính và tách ra các luồng con
        SingleOutputStreamOperator<String> mainStream = env.addSource(
                new ActiveMQSource("tcp://localhost:61616", "report-service-queue")
        ).process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
                // Chuyển đổi JSON thành đối tượng
                ReportCreateRequest request = objectMapper.readValue(value, ReportCreateRequest.class);

                // Phân loại dữ liệu dựa trên loại báo cáo
                switch (ReportOf.valueOf(request.getOf())) {
                    case PRESCRIPTION -> ctx.output(prescriptionTag, request.getData());
                    case APPOINTMENT -> ctx.output(appointmentTag, request.getData());
                    default -> out.collect("Unknown type: " + request.getOf());
                }
            }
        });

        // Trích xuất side outputs
        DataStream<String> prescriptionStream = mainStream.getSideOutput(prescriptionTag);
        DataStream<String> appointmentStream = mainStream.getSideOutput(appointmentTag);

        // Gắn từng luồng con với một sink riêng
        prescriptionStream.addSink(new PrescriptionSaleReportsSink());
//        appointmentStream.addSink(new AppointmentReportsSink());

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
