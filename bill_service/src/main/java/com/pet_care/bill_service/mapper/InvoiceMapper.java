package com.pet_care.bill_service.mapper;

import com.pet_care.bill_service.dto.request.InvoiceCreateRequest;
import com.pet_care.bill_service.dto.request.InvoiceUpdateRequest;
import com.pet_care.bill_service.dto.response.InvoiceResponse;
import com.pet_care.bill_service.model.Invoice;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    /**
     * @param createRequest
     * @return
     */
    Invoice toEntity(InvoiceCreateRequest createRequest);

    /**
     * @param invoice
     * @return
     */
    InvoiceResponse toDto(Invoice invoice);

    /**
     * @param updateRequest
     * @param invoice
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Invoice partialUpdate(InvoiceUpdateRequest updateRequest, @MappingTarget Invoice invoice);
}