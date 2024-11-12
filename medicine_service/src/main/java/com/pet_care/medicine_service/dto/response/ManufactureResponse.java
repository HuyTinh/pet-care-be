package com.pet_care.medicine_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Response DTO representing a manufacture.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManufactureResponse {

    /**
     * The unique identifier of the manufacture.
     */
    Long id;

    /**
     * The name of the manufacture.
     */
    String name;

    /**
     * The status indicating whether the manufacture is active or not.
     */
    Boolean status;
}
