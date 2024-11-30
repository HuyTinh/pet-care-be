package com.pet_care.report_service.service.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet_care.report_service.dto.request.PrescriptionSaleReportRequest;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class PrescriptionSaleReportsSink extends RichSinkFunction<String> {

    private transient Connection connection;
    private transient PreparedStatement preparedStatement;


    @Override
    public void invoke(String value, Context context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();


        if(connection==null){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3317/report_service", "root", "root");
            // Ghi dữ liệu vào MySQL
            preparedStatement = connection.prepareStatement("INSERT INTO prescription_sale_reports (date, sale) VALUES (?, ?)" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "sale = sale + VALUES(sale);");
        }

        if (value != null) {
            PrescriptionSaleReportRequest prescriptionSaleReportRequest = objectMapper.readValue(value, PrescriptionSaleReportRequest.class);
            preparedStatement.setDate(1, new java.sql.Date(prescriptionSaleReportRequest.getDate().getTime()));  // Thiết lập tham số cho tên
            preparedStatement.setDouble(2,prescriptionSaleReportRequest.getSale());  // Thiết lập thời gian hiện tại
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void finish() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
