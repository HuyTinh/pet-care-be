package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Data
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
    Date appointment_date;

    @Column(name = "appointment_time", columnDefinition = "time", nullable = false)
    Time appointment_time;

    @Column(name = "status", nullable = false)
    boolean status;

    @Column(name = "status_accept", nullable = false)
    String status_accept;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    Set<Appointment_Service> appointment_service;

}
