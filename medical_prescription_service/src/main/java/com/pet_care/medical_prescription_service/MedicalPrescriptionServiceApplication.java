package com.pet_care.medical_prescription_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MedicalPrescriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalPrescriptionServiceApplication.class, args);
    }

}
