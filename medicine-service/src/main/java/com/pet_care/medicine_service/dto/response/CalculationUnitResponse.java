package com.pet_care.medicine_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Response DTO representing a calculation unit.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculationUnitResponse {

    /**
     * The unique identifier of the calculation unit.
     */
    Long id;

    /**
     * The name of the calculation unit.
     */
    String name;

    /**
     * The status indicating whether the calculation unit is active or not.
     */
    Boolean status;
}
