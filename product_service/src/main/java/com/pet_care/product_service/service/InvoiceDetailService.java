package com.pet_care.product_service.service;

import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.InvoiceDetail;
import com.pet_care.product_service.model.Product;
import com.pet_care.product_service.repository.InvoiceDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceDetailService
{

    InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceDetail setDataInvoiceDetail(int quantity, Product product, Invoice savedInvoice)
    {

        InvoiceDetail detail = InvoiceDetail.builder()
                .totalPrice(quantity * product.getPrice())
                .quantity(quantity)
                .product(product)
//                .invoice(savedInvoice)
                .createdAt(new Date())
                .build();
        return detail;
//        return invoiceDetailRepository.save(detail);
    }

    public InvoiceDetail createInvoiceDetail(InvoiceDetail invoiceDetail, Invoice savedInvoice)
    {
        invoiceDetail.setInvoice(savedInvoice);
        return invoiceDetailRepository.save(invoiceDetail);
    }

}
