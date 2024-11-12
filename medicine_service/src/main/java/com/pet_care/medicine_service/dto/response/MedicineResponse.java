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

/**
 * Response DTO representing a medicine's details.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineResponse {

    /**
     * The unique identifier of the medicine.
     */
    Long id;

    /**
     * The name of the medicine.
     */
    String name;

    /**
     * The manufacturing date of the medicine.
     */
    @JsonProperty("manufacturing_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date manufacturingDate;

    /**
     * The expiry date of the medicine.
     */
    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date expiryDate;

    /**
     * The date when the medicine was imported.
     */
    @JsonProperty("date_import")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "yyyy-MM-dd")
    Date dateImport;

    /**
     * The available quantity of the medicine.
     */
    Integer quantity;

    /**
     * The price of the medicine.
     */
    Double price;

    /**
     * Additional notes related to the medicine.
     */
    String note;

    /**
     * The image URL of the medicine.
     */
    String image_url;

    /**
     * A set of calculation units associated with the medicine.
     */
    @JsonProperty("calculation_units")
    Set<CalculationUnit> calculationUnits;

    /**
     * The manufacture details of the medicine.
     */
    Manufacture manufacture;

    /**
     * The locations where the medicine is stored.
     */
    Set<Location> locations;

    /**
     * The status of the medicine (e.g., available, out of stock).
     */
    @Enumerated(EnumType.STRING)
    MedicineStatus status;

    /**
     * The type of the medicine (e.g., tablet, liquid).
     */
    MedicineTypes types;
}
