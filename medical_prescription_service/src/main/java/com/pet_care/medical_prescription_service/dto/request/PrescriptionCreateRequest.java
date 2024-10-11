package com.pet_care.medical_prescription_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionCreateRequest {
    @JsonProperty("appointment_id")
    Long appointmentId;
    Set<PrescriptionDetailCreateRequest> details;
    @JsonProperty("total_money")
    Double totalMoney;
}