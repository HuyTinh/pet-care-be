package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.dto.request.sub.AppointmentRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentCreateRequest {
    String email;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("account_id")
    Long accountId;

    AppointmentRequest appointment;
}