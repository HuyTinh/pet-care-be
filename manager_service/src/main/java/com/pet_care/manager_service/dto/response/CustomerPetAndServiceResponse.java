package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerPetAndServiceResponse {
    long id;
    String first_name;
    String last_name;
    String phone_number;
    Set<PetResponse> petResponses;
    Set<ServiceResponse> serviceResponses;
}
