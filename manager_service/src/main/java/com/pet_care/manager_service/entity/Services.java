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
@Table(name="services")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "price", nullable = false)
    double price;

    @Column(name = "status", nullable = false)
    boolean status;

    @ManyToOne
    @JoinColumn(name = "service_type_id", nullable = false)
    Service_Type service_type;

    @OneToMany(mappedBy = "services")
    @JsonIgnore
    Set<Appointment_Service> appointment_service;
}
