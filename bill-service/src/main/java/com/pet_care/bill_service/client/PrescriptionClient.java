package com.pet_care.bill_service.client;

import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.PrescriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@FeignClient(name = "medical-prescription-service")
public interface PrescriptionClient {

    /**
     * @param prescriptionId
     * @return
     */
    @GetMapping("${service.medical-prescription-client.path}/prescription/{prescriptionId}")
    APIResponse<PrescriptionResponse> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId);
}
