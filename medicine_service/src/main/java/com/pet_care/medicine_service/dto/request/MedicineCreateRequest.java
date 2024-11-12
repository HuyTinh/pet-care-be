package com.pet_care.medicine_service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medicine_service.enums.MedicineStatus;
import com.pet_care.medicine_service.enums.MedicineTypes;
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
public class MedicineCreateRequest {

    String name;

    /**
     * The manufacturing date of the medicine.
     *
     * @see DateTimeFormat
     */
    @JsonProperty("manufacturing_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date manufacturingDate;

    /**
     * The expiry date of the medicine.
     *
     * @see DateTimeFormat
     */
    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date expiryDate;

    /**
     * The quantity of the medicine.
     */
    Integer quantity;

    /**
     * The date the medicine was imported.
     *
     * @see DateTimeFormat
     */
    @JsonProperty("date_import")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dateImport;

    /**
     * The price of the medicine.
     */
    Double price;

    /**
     * Set of calculation unit IDs related to the medicine.
     */
    @JsonProperty("calculation_units")
    Set<Long> calculationUnits;

    /**
     * The ID of the manufacturer of the medicine.
     */
    @JsonProperty("manufacture_id")
    Long manufactureId;

    /**
     * Set of location IDs where the medicine is available.
     */
    @JsonProperty("locations")
    Set<Long> locations;

    /**
     * Additional notes about the medicine.
     */
    String note;

    /**
     * The status of the medicine (e.g., available, out of stock).
     */
    MedicineStatus status;

    /**
     * The type of the medicine (e.g., over-the-counter, prescription).
     */
    MedicineTypes types;

}
