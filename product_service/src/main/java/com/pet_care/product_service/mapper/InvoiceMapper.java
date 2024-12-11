package com.pet_care.product_service.mapper;

import com.pet_care.product_service.dto.response.InvoiceDetailResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.model.Invoice;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = InvoiceDetailMapper.class)
public interface InvoiceMapper {

    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    InvoiceResponse mapperToInvoiceResponse(Invoice invoice);
    List<InvoiceResponse> mapperToInvoiceResponses(List<Invoice> invoice);

    @AfterMapping
    default void setDetails(
            Invoice invoice,
            @MappingTarget InvoiceResponse response,
            @Context InvoiceDetailMapper invoiceDetailMapper
    ) {
        List<InvoiceDetailResponse> details = invoiceDetailMapper.mapperToInvoiceDetailResponses(
                invoice.getInvoiceDetails()
        );
        response.setInvoiceDetailResponses(details);
    }
}

