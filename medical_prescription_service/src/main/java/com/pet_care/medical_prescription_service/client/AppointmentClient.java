package com.pet_care.medical_prescription_service.client;


import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.model.Appointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "appointmentClient", url = "http://localhost:8082/api/v1/appointment-service/appointment")
public interface AppointmentClient {
    @GetMapping("/{appointmentId}")
    APIResponse<Appointment> getAppointmentById(@PathVariable("appointmentId") Long appointmentId);
}
