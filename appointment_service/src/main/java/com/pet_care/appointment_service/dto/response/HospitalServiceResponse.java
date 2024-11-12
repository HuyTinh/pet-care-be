package com.pet_care.appointment_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Response object for hospital service details.
 * It contains information about a specific service offered at the hospital.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalServiceResponse {

    // Name of the hospital service
    String name;

    // Description of the hospital service
    String description;

    // Price of the hospital service
    Double price;
}
