package com.pet_care.product_service.mapper;

import com.pet_care.product_service.dto.response.InvoiceDetailResponse;
import com.pet_care.product_service.dto.response.InvoiceResponse;
import com.pet_care.product_service.dto.response.ProductResponse;
import com.pet_care.product_service.model.Invoice;
import com.pet_care.product_service.model.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    /**
     * Converts an Invoice entity to a ProductResponse DTO.
     *
     * @param invoice the Product entity to be converted
     * @return a InvoiceResponse DTO populated with data from the Invoice entity
     */
    @Mapping(target = "invoiceId", source = "id")
    @Mapping(target = "invoiceDetailResponses", source = "invoiceDetails") // Ánh xạ các chi tiết hóa đơn
    @Mapping(target = "statusAccept", source = "statusAccept")
    InvoiceResponse toDto(Invoice invoice);

//    @Mapping(target = "invoiceId", source = "id")
//    @Mapping(target = "createdAt", source = "createdAt")
//    InvoiceResponse mapperToInvoiceResponse(Invoice invoice);
//    List<InvoiceResponse> mapperToInvoiceResponses(List<Invoice> invoice);
//
//    @AfterMapping
//    default void setDetails(
//            Invoice invoice,
//            @MappingTarget InvoiceResponse response,
//            @Context InvoiceDetailMapper invoiceDetailMapper
//    ) {
//        List<InvoiceDetailResponse> details = invoiceDetailMapper.mapperToInvoiceDetailResponses(
//                invoice.getInvoiceDetails()
//        );
//        response.setInvoiceDetailResponses(details);
//    }
}

