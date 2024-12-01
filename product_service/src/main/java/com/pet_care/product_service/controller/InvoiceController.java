package com.pet_care.product_service.controller;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.response.APIResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.enums.StatusAccept;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.repository.InvoiceRepository;
import com.pet_care.product_service.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("invoice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController 
{
    
    InvoiceService invoiceService;

//    InvoiceRepository invoiceRepository;
//
//    @GetMapping("/{page}")
//    public APIResponse<List<Invoice>> getAllInvoice(@PathVariable("page") int _page)
//    {
//        List<Invoice> response = invoiceRepository.findAll();
//
//        return APIResponse.<List<Invoice>>builder()
//                .data(response)
//                .build();
//    }

    @GetMapping("/{page}")
    public APIResponse<PageableResponse> getAllInvoice(@PathVariable("page") int _page)
    {
        PageableResponse response = invoiceService.getAllInvoice(_page);

        return APIResponse.<PageableResponse>builder()
                .data(response)
                .build();
    }

//    @PostMapping("/create")
//    public APIResponse<Invoice> createInvoice(@RequestBody List<InvoiceRequest> _invoiceRequests)
//    {
//        Invoice response = invoiceService.createInvoice(_invoiceRequests);
//
//        return APIResponse.<Invoice>builder()
//                .data(response)
//                .build();
//    }

    @PostMapping("/create")
    public APIResponse<InvoiceResponse> createInvoice(@RequestBody List<InvoiceRequest> _invoiceRequests)
    {
        InvoiceResponse response = invoiceService.createInvoice(_invoiceRequests);

        return APIResponse.<InvoiceResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping("/accept/{invoiceId}")
    public APIResponse<InvoiceResponse> acceptInvoice(@PathVariable("invoiceId") Long _invoiceId)
    {
        InvoiceResponse response = invoiceService.updateStatusInvoice(_invoiceId, StatusAccept.PENDING);

        return APIResponse.<InvoiceResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping("/success/{invoiceId}")
    public APIResponse<InvoiceResponse> successInvoice(@PathVariable("invoiceId") Long _invoiceId)
    {
        InvoiceResponse response = invoiceService.updateStatusInvoice(_invoiceId, StatusAccept.SUCCESS);

        return APIResponse.<InvoiceResponse>builder()
                .data(response)
                .build();
    }

    @PutMapping("/detele/{invoiceId}")
    public APIResponse<InvoiceResponse> deleteInvoice(@PathVariable("invoiceId") Long _invoiceId)
    {
        InvoiceResponse response = invoiceService.deleteInvoice(_invoiceId, StatusAccept.DELETED);

        return APIResponse.<InvoiceResponse>builder()
                .data(response)
                .build();
    }
    
}
