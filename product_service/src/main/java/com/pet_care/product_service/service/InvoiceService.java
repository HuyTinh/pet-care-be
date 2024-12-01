package com.pet_care.product_service.service;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.dto.response.InvoiceDetailResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.dto.response.PageableResponse;
import com.pet_care.product_service.enums.StatusAccept;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.InvoiceDetailMapper;
import com.pet_care.product_service.mapper.InvoiceMapper;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private static final int PAGE_SIZE = 50;

    private final InvoiceRepository invoiceRepository;
    private final ProductService productService;
    private final InvoiceDetailService invoiceDetailService;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceDetailMapper invoiceDetailMapper;

    public PageableResponse getAllInvoice(int page) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), PAGE_SIZE, Sort.by("id").descending());
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);

        List<InvoiceResponse> responses = invoicePage.getContent().stream()
                .map(this::mapToInvoiceResponse)
                .collect(Collectors.toList());

        return buildPageableResponse(invoicePage, responses);
    }

    public InvoiceResponse createInvoice(List<InvoiceRequest> invoiceRequests) {
        Invoice savedInvoice = createInvoiceEntity(invoiceRequests);
        Invoice finalInvoice = saveInvoiceWithDetails(savedInvoice, invoiceRequests);

        return mapToInvoiceResponse(finalInvoice);
    }

    public InvoiceResponse updateStatusInvoice(long id, StatusAccept action) {
        Invoice invoice = findInvoiceById(id);
        validateStatusAccept(invoice.getStatusAccept(), action);

        invoice.setStatusAccept(action);
        Invoice updatedInvoice = invoiceRepository.save(invoice);

        return mapToInvoiceResponse(updatedInvoice);
    }

    public InvoiceResponse deleteInvoice(long id, StatusAccept status) {
        Invoice invoice = findInvoiceById(id);
        validateStatusAccept(invoice.getStatusAccept(), status);

        invoice.setStatusAccept(StatusAccept.DELETED);
        handleProductQuantityDuringDeletion(invoice);

        Invoice deletedInvoice = invoiceRepository.save(invoice);

        return mapToInvoiceResponse(deletedInvoice);
    }

    private InvoiceResponse mapToInvoiceResponse(Invoice invoice) {
        InvoiceResponse response = invoiceMapper.mapperToInvoiceResponse(invoice);
        List<InvoiceDetailResponse> detailResponse = invoiceDetailMapper.mapperToInvoiceDetailResponses(invoice.getInvoiceDetails());
        response.setInvoiceDetailResponses(detailResponse);
        return response;
    }

    private PageableResponse buildPageableResponse(Page<Invoice> invoicePage, List<InvoiceResponse> responses) {
        return PageableResponse.builder()
                .totalPages(invoicePage.getTotalPages())
                .pageNumber(invoicePage.getNumber())
                .pageSize(invoicePage.getSize())
                .content(Collections.singletonList(responses))
                .build();
    }

    private Invoice createInvoiceEntity(List<InvoiceRequest> invoiceRequests) {
        double totalPrice = 0;
        Invoice savedInvoice = Invoice.builder()
                .note(invoiceRequests.get(0).getNote())
                .statusAccept(StatusAccept.CREATED)
                .createdAt(new Date())
                .totalPrice(totalPrice)
                .build();

        return invoiceRepository.save(savedInvoice);
    }

    private Invoice saveInvoiceWithDetails(Invoice savedInvoice, List<InvoiceRequest> invoiceRequests) {
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        double totalPrice = 0;

        for (InvoiceRequest invoiceRequest : invoiceRequests) {
            Product product = getProductValidQuantityAndSave(invoiceRequest.getProductId(), invoiceRequest.getQuantity(), "create");
            InvoiceDetail detail = invoiceDetailService.setDataInvoiceDetail(invoiceRequest.getQuantity(), product, savedInvoice);
            totalPrice += invoiceRequest.getQuantity() * product.getPrice();
            invoiceDetails.add(detail);
        }

        savedInvoice.setTotalPrice(totalPrice);
        savedInvoice.setInvoiceDetails(invoiceDetails);

        return invoiceRepository.save(savedInvoice);
    }

    private Invoice findInvoiceById(long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new APIException(ErrorCode.INVOICE_NOT_FOUND));
    }

    private void validateStatusAccept(StatusAccept currentStatus, StatusAccept action) {
        if (!validStatusAccept(currentStatus, action)) {
            throw new APIException(ErrorCode.INVALID_STATUS_ACCEPT);
        }
    }

    private boolean validStatusAccept(StatusAccept currentStatus, StatusAccept action) {
        switch (action) {
            case PENDING:
                return currentStatus == StatusAccept.CREATED;
            case SUCCESS:
                return currentStatus == StatusAccept.PENDING;
            case DELETED:
                return currentStatus == StatusAccept.CREATED;
            default:
                return false;
        }
    }

    private void handleProductQuantityDuringDeletion(Invoice invoice) {
        invoice.getInvoiceDetails().forEach(detail -> {
            try {
                getProductValidQuantityAndSave(detail.getProduct().getId(), detail.getQuantity(), "delete");
            } catch (Exception e) {
                throw new APIException(ErrorCode.INVALID_DELETE_INVOICE);
            }
        });
    }

    private Product getProductValidQuantityAndSave(long productId, int quantity, String requirement) {
        Product product = productService.getProductByIdToMakeInvoice(productId);
        int finalQuantity = calculateProductQuantity(product, quantity, requirement);
        product.setQuantity(finalQuantity);
        return productService.updateProductQuantity(product);
    }

    private int calculateProductQuantity(Product product, int quantity, String requirement) {
        int finalQuantity = 0;
        switch (requirement) {
            case "create":
                finalQuantity = product.getQuantity() - quantity;
                if (finalQuantity < 0) {
                    throw new APIException(ErrorCode.INVALID_QUANTITY);
                }
                break;
            case "delete":
                finalQuantity = product.getQuantity() + quantity;
                break;
            default:
                throw new APIException(ErrorCode.INVALID_QUANTITY);
        }
        return finalQuantity;
    }
}
