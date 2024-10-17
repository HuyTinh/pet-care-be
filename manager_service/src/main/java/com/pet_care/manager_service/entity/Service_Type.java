package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="service_types")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Service_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "status", nullable = false)
    boolean status;

    @OneToMany(mappedBy = "service_type")
    @JsonIgnore
    Set<Services> services;
}
