package com.pet_care.payment_service.client;

import com.pet_care.payment_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.payment_service.dto.response.APIResponse;
import com.pet_care.payment_service.dto.response.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "billClient", url = "http://localhost:8088/api/v1/bill-service/invoice")
public interface BillClient {

    /**
     * @param descriptionCode
     * @return
     */
    @GetMapping("{descriptionCode}/description")
    APIResponse<InvoiceResponse> getInvoiceByDescriptionCode(@PathVariable("descriptionCode") String descriptionCode);


    /**
     * @param id
     * @return
     */
    @GetMapping("{id}")
    APIResponse<InvoiceResponse> getInvoiceById(@PathVariable("id") Long id);


    /**
     * @param invoiceUpdatePayOSIdRequest
     * @return
     */
    @PutMapping("payOSId")
    APIResponse<InvoiceResponse> updateInvoicePayOSId(@RequestBody InvoiceUpdatePayOSIdRequest invoiceUpdatePayOSIdRequest);

    /**
     * @param payOSId
     * @return
     */
    @GetMapping("{payOSId}/payOSId")
    APIResponse<Long> getInvoiceIdByOSId(@PathVariable("payOSId") String payOSId);

    /**
     * @param invoiceId
     * @return
     */
    @PutMapping("{invoiceId}/approved")
    APIResponse<?> approveInvoice(@PathVariable("invoiceId") Long invoiceId);

    /**
     * @param invoiceId
     * @return
     */
    @PutMapping("{invoiceId}/canceled")
    APIResponse<?> cancelInvoice(@PathVariable("invoiceId") Long invoiceId);
}
