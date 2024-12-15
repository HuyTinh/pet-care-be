package com.pet_care.product_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceDetailResponse
{

    @JsonProperty("invoice_detail_id")
    Long invoiceDetailId;

    int quantity;

    @JsonProperty("total_price")
    Double totalPrice;

    Double price;
    @JsonProperty("product_id")
    Long productId;

    @JsonProperty("product_name")
    String productName;

    @JsonProperty("product_description")
    String productDescription;

}
