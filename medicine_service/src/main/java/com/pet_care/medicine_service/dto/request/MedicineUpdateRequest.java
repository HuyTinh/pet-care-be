package com.pet_care.medicine_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.enums.MedicineStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineUpdateRequest {

    String name;

    @JsonProperty("manufacturing_date")
    Date manufacturingDate;

    @JsonProperty("expiry_date")
    Date expiryDate;

    Integer quantity;

    Double price;

    Set<Long> calculationUnits;

    Set<Long> manufactures;

    Set<Long> locations;

    String note;

    @Enumerated(EnumType.STRING)
    MedicineStatus status;
}
