package com.pet_care.medical_prescription_service.dto.response;


import com.fasterxml.jackson.annotation.*;
import com.pet_care.medical_prescription_service.enums.AppointmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentResponse {
    Long id;

    @JsonProperty("account_id")
    Long accountId;

    String account;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+07:00", pattern = "yyyy-MM-dd")
    Date appointmentDate;

    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+08:00", pattern = "HH:mm")
    Date appointmentTime;

    @JsonIgnore
    AppointmentStatus status;
    
    Set<PetResponse> pets;
}

