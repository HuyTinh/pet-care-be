package com.pet_care.payment_service.client;

import com.pet_care.payment_service.dto.response.APIResponse;
import com.pet_care.payment_service.dto.response.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "billClient", url = "http://localhost:8090/api/v1/bill-service")
public interface BillClient {

    /**
     * @param descriptionCode
     * @return
     */
    @GetMapping("/bill/{descriptionCode}/description")
    APIResponse<InvoiceResponse> getBillByDescriptionCode(@PathVariable("descriptionCode") String descriptionCode);


    @GetMapping("bill/{id}")
    APIResponse<InvoiceResponse> getBillById(@PathVariable("id") Long id);
}
