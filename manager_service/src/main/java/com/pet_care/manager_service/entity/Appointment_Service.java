package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
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
    @JoinColumn(name = "services_id", nullable = false)
    Services services;

    @Column(name = "price", nullable = false)
    double price;

    @OneToOne(mappedBy = "appointment_service")
    @JsonIgnore
    Invoice_Service_Detail invoiceServiceDetails;
}
