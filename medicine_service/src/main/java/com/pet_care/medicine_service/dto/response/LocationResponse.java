package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Response DTO representing a location.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {

    /**
     * The unique identifier of the location.
     */
    Long id;

    /**
     * The area name where the location is situated.
     */
    String area;

    /**
     * The row location within the area.
     */
    @JsonProperty("row_location")
    Integer rowLocation;

    /**
     * The column location within the area.
     */
    @JsonProperty("column_location")
    Integer columnLocation;

    /**
     * The status indicating whether the location is active or not.
     */
    Boolean status;
}
