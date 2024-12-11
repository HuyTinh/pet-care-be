package com.pet_care.notification_service.dto.response;

// Import necessary Lombok annotations for generating methods and configuring fields
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetResponse { // Class to represent pet details in a response

    Long id; // Unique identifier for the pet

    String name; // Name of the pet

    String age; // Age of the pet

    Double weight; // Weight of the pet

    String species; // Species of the pet (e.g., dog, cat)
}
