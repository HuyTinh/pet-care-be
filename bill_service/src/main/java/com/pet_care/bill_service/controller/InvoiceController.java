package com.pet_care.bill_service.controller;

import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdateRequest;
import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {

    InvoiceService invoiceService;

    /**
     * @return
     */
    @GetMapping
    public  APIResponse<List<InvoiceResponse>> getAllInvoice() {

        return APIResponse.<List<InvoiceResponse>>builder()
                .data(invoiceService.getAllInvoice())
                .build();
    }

    /**
     * @param invoiceId
     * @return
     */
    @GetMapping("{invoiceId}")
    public  APIResponse<InvoiceResponse> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
        return APIResponse.<InvoiceResponse>builder()
                .data(invoiceService.getInvoiceById(invoiceId))
                .build();
    }

    /**
     * @param invoiceCreateRequest
     * @return
     */
    @PostMapping
    public  APIResponse<InvoiceResponse> createInvoice(@RequestBody InvoiceCreateRequest invoiceCreateRequest){
        return APIResponse.<InvoiceResponse>builder()
                .data(invoiceService.createInvoice(invoiceCreateRequest))
                .build();
    }

    /**
     * @param invoiceUpdatePayOSIdRequest
     * @return
     */
    @PutMapping("payOSId")
    public  APIResponse<InvoiceResponse> updateInvoicePayOSId(@RequestBody InvoiceUpdatePayOSIdRequest invoiceUpdatePayOSIdRequest){
        return  APIResponse.<InvoiceResponse>builder()
                .data(invoiceService.updateInvoicePayOSId(invoiceUpdatePayOSIdRequest))
                .build();
    }

    /**
     * @param payOSId
     * @return
     */
    @GetMapping("{payOSId}/payOSId")
    public APIResponse<Long> getInvoiceIdByOSId(@PathVariable("payOSId") String payOSId){
        return  APIResponse.<Long>builder()
                .data(invoiceService.getInvoiceIdByPayOSId(payOSId))
                .build();
    }
}
