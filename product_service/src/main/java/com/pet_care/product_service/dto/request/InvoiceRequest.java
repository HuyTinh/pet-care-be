package com.pet_care.product_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceRequest
{

    @JsonProperty("product_id")
    Long productId;

    int quantity;

    String note;

}
