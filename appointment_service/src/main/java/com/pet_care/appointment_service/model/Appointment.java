package com.pet_care.appointment_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "appointments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("email")
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("appointment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    Date appointmentDate;

    @JsonProperty("appointment_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Temporal(TemporalType.TIME)
    Date appointmentTime;

    @ManyToMany
    Set<HospitalServiceEntity> services;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @Builder.Default
    Date createdAt = new Date();

    @Builder.Default
    Date updatedAt = new Date();
}
