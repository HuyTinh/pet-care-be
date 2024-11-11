package com.pet_care.notification_service.client;


import com.pet_care.notification_service.dto.response.APIResponse;
import com.pet_care.notification_service.dto.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@FeignClient(name = "appointmentClient", url = "http://localhost:8082/api/v1/appointment-service")
public interface AppointmentClient {
    /**
     * @return
     */
    @GetMapping("appointment/up-coming")
    APIResponse<List<AppointmentResponse>> getAllAppointmentUpComing();

}
