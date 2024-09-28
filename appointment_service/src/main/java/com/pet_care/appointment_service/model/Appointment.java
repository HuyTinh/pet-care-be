package com.pet_care.appointment_service.model;

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

    @JsonProperty("customer_id")
    Long customerId;

    @JsonProperty("appointment_date")
    Date appointmentDate;

    @ManyToMany
    Set<HospitalServiceEntity> services;

    @Enumerated(EnumType.STRING)
    AppointmentStatus status;

    @Builder.Default
    Date createdAt = new Date();

    @Builder.Default
    Date updatedAt = new Date();
}
