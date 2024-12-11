package com.pet_care.product_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceResponse
{

    @JsonProperty("invoice_id")
    Long invoiceId;

    @JsonProperty("created_at")
    Date createdAt;

    List<InvoiceDetailResponse> invoiceDetailResponses;

}
