package com.pet_care.report_service.service.sink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class PrescriptionSaleReportsSink extends RichSinkFunction<String> {

    private transient Connection connection;
    private transient PreparedStatement preparedStatement;

    @Override
    public void invoke(String value, Context context) throws Exception {

        if(connection==null){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3317/report_service", "root", "root");
            // Ghi dữ liệu vào MySQL
            preparedStatement = connection.prepareStatement("INSERT INTO prescription_sale_reports (name, created_at) VALUES (?, ?)");
        }

        if (value != null) {
            preparedStatement.setString(1, value);  // Thiết lập tham số cho tên
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));  // Thiết lập thời gian hiện tại
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
