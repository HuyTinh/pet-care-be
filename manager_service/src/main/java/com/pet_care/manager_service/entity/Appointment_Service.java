package com.pet_care.manager_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="appointment_services")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment_Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    Services services;
}
