package com.pet_care.medical_prescription_service.client;


import com.pet_care.medical_prescription_service.dto.response.APIResponse;
import com.pet_care.medical_prescription_service.dto.response.AppointmentResponse;
import com.pet_care.medical_prescription_service.dto.response.PetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Service
@FeignClient(name = "appointment-service")
public interface AppointmentClient {
    /**
     * @param appointmentId
     * @return
     */
    @GetMapping("${service.appointment-client.path}/appointment/{appointmentId}")
    APIResponse<AppointmentResponse>getAppointmentById (@PathVariable("appointmentId") Long appointmentId);

    /**
     * @param appointmentId
     * @param services
     * @return
     */
    @PutMapping("${service.appointment-client.path}/appointment/{appointmentId}/service")
    APIResponse<AppointmentResponse> updateAppointmentService(@PathVariable("appointmentId") Long appointmentId, @RequestBody Set<String> services);

    /**
     * @param petId
     * @return
     */
    @GetMapping("${service.appointment-client.path}/pet/{petId}")
    APIResponse<PetResponse> getPetById(@PathVariable("petId") Long petId);


    /**
     * @param appointmentId
     * @return
     */
    @PostMapping("${service.appointment-client.path}/appointment/approved/{appointmentId}")
    APIResponse<Integer> approvedAppointment(@PathVariable Long appointmentId);

}
