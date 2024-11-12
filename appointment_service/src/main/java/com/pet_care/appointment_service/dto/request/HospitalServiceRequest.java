package com.pet_care.appointment_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO class for creating a new hospital service request. This class holds the necessary data
 * to request a new service in the system.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalServiceRequest {

    // Name of the hospital service
    String name;

    // Description of the hospital service
    String description;
}
