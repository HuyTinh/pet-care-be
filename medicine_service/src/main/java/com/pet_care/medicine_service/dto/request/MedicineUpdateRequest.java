package com.pet_care.medicine_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineUpdateRequest {

    /**
     * The name of the medicine to update.
     */
    String name;

    /**
     * The manufacturing date of the medicine.
     */
    @JsonProperty("manufacturing_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date manufacturingDate;

    /**
     * The expiry date of the medicine.
     */
    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date expiryDate;

    /**
     * The import date of the medicine.
     */
    @JsonProperty("date_import")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateImport;

    /**
     * The quantity of the medicine.
     */
    Integer quantity;

    /**
     * The price of the medicine.
     */
    Double price;

    /**
     * The list of calculation units associated with the medicine.
     */
    @JsonProperty("calculation_units")
    Set<Long> calculationUnits;

    /**
     * The ID of the manufacturer of the medicine.
     */
    @JsonProperty("manufacture_id")
    Long manufactureId;

    /**
     * The list of locations where the medicine is available.
     */
    @JsonProperty("locations")
    Set<Long> locations;

    /**
     * Additional notes for the medicine.
     */
    String note;

    /**
     * The status of the medicine.
     */
    @Enumerated(EnumType.STRING)
    MedicineStatus status;

    /**
     * The type of the medicine.
     */
    @Enumerated(EnumType.STRING)
    MedicineTypes types;
}
