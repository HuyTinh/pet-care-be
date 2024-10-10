package com.pet_care.bill_service.mapper;

import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdateRequest;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.model.Invoice;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    Invoice toEntity(InvoiceCreateRequest createRequest);

    InvoiceResponse toDto(Invoice invoice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invoice partialUpdate(InvoiceUpdateRequest updateRequest, @MappingTarget Invoice invoice);
}