package com.pet_care.medicine_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineUpdateQtyRequest {

    /**
     * The ID of the medicine to update.
     */
    @JsonProperty("medicine_id")
    Long medicineId;

    /**
     * The new quantity for the medicine.
     */
    Long qty;

}
