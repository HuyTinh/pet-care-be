package com.pet_care.medical_prescription_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowUpCreateRequest {

    @JsonProperty("follow_up_date")
    Date followUpDate;

    @JsonProperty("follow_up_time")
    Date followUpTime;

    String reason;

    String notes;

    @JsonProperty("appointment_id")
    Long appointmentId;
}
