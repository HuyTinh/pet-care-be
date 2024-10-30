package com.pet_care.bill_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.bill_service.enums.PrescriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionResponse {
    Long id;

    @JsonProperty("appointment")
    AppointmentResponse appointmentResponse;

    @JsonProperty("details")
    Set<PetPrescriptionResponse> details;

    @Enumerated(EnumType.STRING)
    PrescriptionStatus status;

    @JsonProperty("total_money")
    Double totalMoney;
}
