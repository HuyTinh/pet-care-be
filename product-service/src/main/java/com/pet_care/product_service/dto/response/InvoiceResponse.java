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
    @JsonProperty("total_price")
    Double totalPrice;

    String note;

    Long customer_id;

    @JsonProperty("full_name")
    String fullName;
    @JsonProperty("phone_number")
    String phoneNumber;

    String address;
    @JsonProperty("created_at")
    Date createdAt;
    @JsonProperty("status_accept")
    String statusAccept;

    List<InvoiceDetailResponse> invoiceDetailResponses;

}
