package com.pet_care.medicine_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    Long id;

    String area;

    @JsonProperty("row_location")
    Integer rowLocation;

    @JsonProperty("column_location")
    Integer columnLocation;

    Boolean status;
}
