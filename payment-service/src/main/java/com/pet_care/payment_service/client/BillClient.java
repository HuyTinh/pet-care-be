package com.pet_care.payment_service.client;

import com.pet_care.payment_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.payment_service.dto.response.APIResponse;
import com.pet_care.payment_service.dto.response.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@FeignClient(name = "bill-service")
public interface BillClient {

    /**
     * @param descriptionCode
     * @return
     */
    @GetMapping("${service.bill-client.path}/invoice/{descriptionCode}/description")
    APIResponse<InvoiceResponse> getInvoiceByDescriptionCode(@PathVariable("descriptionCode") String descriptionCode);


    /**
     * @param id
     * @return
     */
    @GetMapping("${service.bill-client.path}/invoice/{id}")
    APIResponse<InvoiceResponse> getInvoiceById(@PathVariable("id") Long id);


    /**
     * @param invoiceUpdatePayOSIdRequest
     * @return
     */
    @PutMapping("${service.bill-client.path}/invoice/payOSId")
    APIResponse<InvoiceResponse> updateInvoicePayOSId(@RequestBody InvoiceUpdatePayOSIdRequest invoiceUpdatePayOSIdRequest);

    /**
     * @param payOSId
     * @return
     */
    @GetMapping("${service.bill-client.path}/invoice/{payOSId}/payOSId")
    APIResponse<Long> getInvoiceIdByOSId(@PathVariable("payOSId") String payOSId);

    /**
     * @param invoiceId
     * @return
     */
    @PutMapping("${service.bill-client.path}/invoice/{invoiceId}/approved")
    APIResponse<?> approveInvoice(@PathVariable("invoiceId") Long invoiceId);

    /**
     * @param invoiceId
     * @return
     */
    @PutMapping("${service.bill-client.path}/invoice/{invoiceId}/canceled")
    APIResponse<?> cancelInvoice(@PathVariable("invoiceId") Long invoiceId);
}
