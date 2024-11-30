package com.pet_care.product_service.service;

import com.pet_care.product_service.dto.request.InvoiceRequest;
import com.pet_care.product_service.exception.APIException;
import com.pet_care.product_service.exception.ErrorCode;
import com.pet_care.product_service.mapper.ProductMapper;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.InvoiceRepository;
import com.pet_care.product_service.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService
{

    InvoiceRepository invoiceRepository;

    ProductService productService;

    InvoiceDetailService invoiceDetailService;

    public void createInvoice(List<InvoiceRequest> invoiceRequests)
    {
        Set<InvoiceDetail> invoiceDetails = new HashSet<>();
        Invoice savedInvoice = new Invoice();
        double totalPrice = 0;
        for (InvoiceRequest invoiceRequest : invoiceRequests)
        {

            Product product = getProductValidQuantityAndSave(invoiceRequest);
            InvoiceDetail detail = invoiceDetailService.setDataInvoiceDetail(invoiceRequest.getQuantity(), product, savedInvoice);
            totalPrice += invoiceRequest.getQuantity() * product.getPrice();
            invoiceDetails.add(detail);

        }
        savedInvoice = Invoice.builder()
                .note(invoiceRequests.get(0).getNote())
//                .invoiceDetails(invoiceDetails)
                .totalPrice(totalPrice)
                .createdAt(new Date())
                .build();

        invoiceRepository.save(savedInvoice);
        for (InvoiceDetail invoiceDetail : invoiceDetails)
        {
            invoiceDetailService.createInvoiceDetail(invoiceDetail, savedInvoice);

        }

    }

    public Product getProductValidQuantityAndSave(InvoiceRequest invoiceRequest)
    {
        Product product = productService.getProductByIdToMakeInvoice(invoiceRequest.getProductId());
        int finalQuantity = product.getQuantity() - invoiceRequest.getQuantity();
        if(finalQuantity < 0)
        {
            throw new APIException(ErrorCode.INVALID_QUANTITY);
        }
        product.setQuantity(finalQuantity);

        return productService.updateProductQuantity(product);
    }

}