package com.pet_care.bill_service.controller;

import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.service.InvoiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public @NotNull APIResponse<List<InvoiceResponse>> getAllInvoice() {

        return APIResponse.<List<InvoiceResponse>>builder()
                .data(invoiceService.getAllInvoice())
                .build();
    }

    /**
     * @param invoiceId
     * @return
     */
    @GetMapping("{invoiceId}")
    public @NotNull APIResponse<InvoiceResponse> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
        return APIResponse.<InvoiceResponse>builder()
                .data(invoiceService.getInvoiceById(invoiceId))
                .build();
    }
}
