package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import com.pet_care.medicine_service.model.CalculationUnit;
import com.pet_care.medicine_service.model.Location;
import com.pet_care.medicine_service.model.Manufacture;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date manufacturingDate;

    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date expiryDate;

    @JsonProperty("date_import")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date dateImport;

    Integer quantity;

    Double price;

    String note;

    String image_url;

    @JsonProperty("calculation_units")
    Set<CalculationUnit> calculationUnits;

    Manufacture manufacture;

    Set<Location> locations;

    @Enumerated(EnumType.STRING)
    MedicineStatus status;

    MedicineTypes types;
}
