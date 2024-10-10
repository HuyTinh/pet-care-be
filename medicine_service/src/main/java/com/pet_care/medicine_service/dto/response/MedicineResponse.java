package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineResponse {
    Long id;

    String name;

    @JsonProperty("manufacturing_date")
    Date manufacturingDate;

    @JsonProperty("expiry_date")
    Date expiryDate;

    Integer quantity;

    Double price;

    String note;

    Boolean status;
}
