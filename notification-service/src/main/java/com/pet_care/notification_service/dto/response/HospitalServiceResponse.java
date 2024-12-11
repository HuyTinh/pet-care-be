package com.pet_care.notification_service.dto.response;

// Import necessary Lombok annotations for generating methods and configuring fields
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Sets all fields to private access level by default
public class HospitalServiceResponse { // Class to represent hospital service details in a response

    String name; // Name of the hospital service

    String description; // Description of the hospital service

    Double price; // Price of the hospital service
}
