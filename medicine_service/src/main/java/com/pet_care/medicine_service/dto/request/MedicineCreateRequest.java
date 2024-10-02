package com.pet_care.medicine_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.model.CalculationUnit;
import com.pet_care.medicine_service.model.Location;
import com.pet_care.medicine_service.model.Manufacture;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineCreateRequest {
    
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

    Boolean status;
}
