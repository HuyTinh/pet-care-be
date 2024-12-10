package com.pet_care.medical_prescription_service.client;


import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.AppointmentResponse;
import com.pet_care.medical_prescription_service.dto.response.PetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Service
@FeignClient(name = "APPOINTMENT-SERVICE")
public interface AppointmentClient {
    /**
     * @param appointmentId
     * @return
     */
    @GetMapping("/api/v1/appointment-service/appointment/{appointmentId}")
    APIResponse<AppointmentResponse>getAppointmentById (@PathVariable("appointmentId") Long appointmentId);

    /**
     * @param appointmentId
     * @param services
     * @return
     */
    @PutMapping("/api/v1/appointment-service/appointment/{appointmentId}/service")
    APIResponse<AppointmentResponse> updateAppointmentService(@PathVariable("appointmentId") Long appointmentId, @RequestBody Set<String> services);

    /**
     * @param petId
     * @return
     */
    @GetMapping("/api/v1/appointment-service/pet/{petId}")
    APIResponse<PetResponse> getPetById(@PathVariable("petId") Long petId);


    /**
     * @param appointmentId
     * @return
     */
    @PostMapping("/api/v1/appointment-service/appointment/approved/{appointmentId}")
    APIResponse<Integer> approvedAppointment(@PathVariable Long appointmentId);

}
