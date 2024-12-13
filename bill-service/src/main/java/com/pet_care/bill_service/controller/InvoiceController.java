package com.pet_care.bill_service.controller;

import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdatePayOSIdRequest;
import com.pet_care.bill_service.dto.response.APIResponse;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.service.InvoiceService;
import com.pet_care.bill_service.service.MessageBrokerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // Lombok annotation to create a logger instance
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoice")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {

    InvoiceService invoiceService;

    MessageBrokerService messageBrokerService;

    /**
     * Retrieves a list of all invoices.
     *
     * @return API response containing a list of InvoiceResponse objects.
     */
    @GetMapping
    public APIResponse<List<InvoiceResponse>> getAllInvoice() {
        log.info("Fetching all invoices.");
        List<InvoiceResponse> invoices = invoiceService.getAllInvoice();
        log.debug("Fetched {} invoices.", invoices.size());
        return APIResponse.<List<InvoiceResponse>>builder()
                .data(invoices)
                .build();
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice to retrieve.
     * @return API response containing the InvoiceResponse object.
     */
    @GetMapping("{invoiceId}")
    public APIResponse<InvoiceResponse> getInvoiceById(@PathVariable("invoiceId") Long invoiceId) {
        log.info("Fetching invoice with ID: {}", invoiceId);
        InvoiceResponse invoice = invoiceService.getInvoiceById(invoiceId);
        log.debug("Fetched invoice: {}", invoice);
        return APIResponse.<InvoiceResponse>builder()
                .data(invoice)
                .build();
    }

    /**
     * Creates a new invoice.
     *
     * @param invoiceCreateRequest the request object containing details for the new invoice.
     * @return API response containing the created InvoiceResponse object.
     */
    @PostMapping
    public APIResponse<InvoiceResponse> createInvoice(@RequestBody InvoiceCreateRequest invoiceCreateRequest) {
        log.info("Creating a new invoice with details: {}", invoiceCreateRequest);
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoiceCreateRequest);
        log.debug("Invoice created successfully: {}", createdInvoice);
        return APIResponse.<InvoiceResponse>builder()
                .data(createdInvoice)
                .build();
    }

    /**
     * Updates the payment OS ID of an invoice.
     *
     * @param invoiceUpdatePayOSIdRequest the request object containing the new payment OS ID details.
     * @return API response containing the updated InvoiceResponse object.
     */
    @PutMapping("payOSId")
    public APIResponse<InvoiceResponse> updateInvoicePayOSId(@RequestBody InvoiceUpdatePayOSIdRequest invoiceUpdatePayOSIdRequest) {
        log.info("Updating payment OS ID with details: {}", invoiceUpdatePayOSIdRequest);
        InvoiceResponse updatedInvoice = invoiceService.updateInvoicePayOSId(invoiceUpdatePayOSIdRequest);
        log.debug("Updated invoice: {}", updatedInvoice);
        return APIResponse.<InvoiceResponse>builder()
                .data(updatedInvoice)
                .build();
    }

    /**
     * Retrieves the invoice ID by its payment OS ID.
     *
     * @param payOSId the payment OS ID.
     * @return API response containing the invoice ID.
     */
    @GetMapping("{payOSId}/payOSId")
    public APIResponse<Long> getInvoiceIdByOSId(@PathVariable("payOSId") String payOSId) {
        log.info("Fetching invoice ID by payment OS ID: {}", payOSId);
        Long invoiceId = invoiceService.getInvoiceIdByPayOSId(payOSId);
        log.debug("Fetched invoice ID: {}", invoiceId);
        return APIResponse.<Long>builder()
                .data(invoiceId)
                .build();
    }

    /**
     * Approves an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice to approve.
     * @return API response with a message indicating success or failure.
     */
    @PutMapping("{invoiceId}/approved")
    public APIResponse<?> approveInvoice(@PathVariable("invoiceId") Long invoiceId) {
        log.info("Approving invoice with ID: {}", invoiceId);
        String message = "Approve Invoice Id: " + invoiceId + " fail";

        Integer isApproved = invoiceService.approvedInvoice(invoiceId);
        if (isApproved == 1) {
            message = "Approve Invoice Id: " + invoiceId + " success";
        }

        log.debug("Approval status for invoice ID {}: {}", invoiceId, message);
        return APIResponse.builder()
                .message(message)
                .build();
    }

    /**
     * Cancels an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice to cancel.
     * @return API response with a message indicating success or failure.
     */
    @PutMapping("{invoiceId}/cancelled")
    public APIResponse<?> cancelInvoice(@PathVariable("invoiceId") Long invoiceId) {
        log.info("Canceling invoice with ID: {}", invoiceId);
        String message = "Cancel Invoice Id: " + invoiceId + " fail";

        Integer isCanceled = invoiceService.canceledInvoice(invoiceId);
        if (isCanceled == 1) {
            message = "Cancel Invoice Id: " + invoiceId + " success";
        }

        log.debug("Cancellation status for invoice ID {}: {}", invoiceId, message);
        return APIResponse.builder()
                .message(message)
                .build();
    }

    @PostMapping(value = "report")
    public APIResponse<?> reportSale(
            @RequestParam("name") String name
    ) {

        messageBrokerService.sendEvent("sale-report-queue", name);
        return APIResponse.builder()
                .message("Send event success")
                .build();
    }

}