package com.pet_care.product_service.mapper;

import com.pet_care.product_service.dto.response.InvoiceDetailResponse;
import com.pet_care.product_service.model.InvoiceDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceDetailMapper
{

    @Mapping(target = "invoiceDetailId", source = "id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productDescription", source = "product.description")
    InvoiceDetailResponse mapperToInvoiceDetailResponse(InvoiceDetail invoiceDetail);
    List<InvoiceDetailResponse> mapperToInvoiceDetailResponses(List<InvoiceDetail> invoiceDetail);

}
