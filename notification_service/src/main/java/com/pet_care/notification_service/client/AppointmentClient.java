package com.pet_care.notification_service.client;

// Import necessary classes for response data and Feign client
import com.pet_care.notification_service.dto.response.APIResponse;
import com.pet_care.notification_service.dto.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service // Marks this interface as a Spring Service for dependency injection
@FeignClient(name = "appointmentClient", url = "http://localhost:8082/api/v1/appointment-service")
// Defines this interface as a Feign client to communicate with the appointment service
public interface AppointmentClient {

    /**
     * Retrieves a list of upcoming appointments
     *
     * @return APIResponse containing a list of AppointmentResponse objects for all upcoming appointments
     */
    @GetMapping("appointment/up-coming")
    // Maps the HTTP GET request to fetch upcoming appointments
    APIResponse<List<AppointmentResponse>> getAllAppointmentUpComing();

}
