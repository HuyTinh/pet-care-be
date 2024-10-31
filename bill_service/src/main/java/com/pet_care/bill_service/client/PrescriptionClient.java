package com.pet_care.bill_service.client;

import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.PrescriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "uploadClient", url = "http://localhost:8086/api/v1/medical-prescription-service")
public interface PrescriptionClient {

    /**
     * @param prescriptionId
     * @return
     */
    @GetMapping("/prescription/{prescriptionId}")
    APIResponse<PrescriptionResponse> getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId);
}
