package com.pet_care.customer_service.dto.request.sub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentRequest {
    @JsonProperty("customer_id")
    Long customerId;

    Set<String> services;

    @JsonProperty("appointment_date")
    Date appointmentDate;

    String status;

    Set<PetCreateRequest> pets;
}
