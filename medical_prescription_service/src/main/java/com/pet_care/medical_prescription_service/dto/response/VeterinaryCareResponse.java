package com.pet_care.medical_prescription_service.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VeterinaryCareResponse {
    String name; // Name of the veterinary care service

    // Detailed description of the veterinary care service
    String description;

    // Enum representing the status of the veterinary care (e.g., ACTIVE, INACTIVE)
    String status;

    // Price of the veterinary care service
    Double price;
}
