package com.pet_care.report_service.config;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ServiceConfig {
    @Bean
    public StreamExecutionEnvironment streamExecutionEnvironment() {
        return StreamExecutionEnvironment.createLocalEnvironment();
    }

    @Bean
    public JdbcConnectionOptions jdbcConnectionOptions() {
        return new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://localhost:3317/report_service")
                .withDriverName("com.mysql.cj.jdbc.Driver")
                .withUsername("root")
                .withPassword("root")
                .build();
    };

    @Bean
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3317/report_service", "root", "root");
    }
}
