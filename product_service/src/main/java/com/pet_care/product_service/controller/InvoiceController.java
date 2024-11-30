package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("invoice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController 
{
    
    InvoiceService invoiceService;
    
    @PostMapping()
    public APIResponse<String> createInvoice(@RequestBody List<InvoiceRequest> _invoiceRequests)
    {
        invoiceService.createInvoice(_invoiceRequests);

        return APIResponse.<String>builder()
                .data("success")
                .build();
    }
    
}
