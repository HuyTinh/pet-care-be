package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pet_care.manager_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="appointments")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "appointment_date", columnDefinition = "date", nullable = false)
    LocalDate appointment_date;

    @Column(name = "appointment_hour", columnDefinition = "time", nullable = false)
    LocalTime appointment_hour;

    @Column(name = "status", nullable = false)
    boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_accept", nullable = false)
    AppointmentStatus status_accept;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    Set<Appointment_Service> appointment_services;

}
