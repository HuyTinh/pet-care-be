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
@Table(name="pets")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "age", nullable = false)
    String age;

    @Column(name = "weight", columnDefinition = "decimal(5,2)", nullable = false)
    float weight;

    @Column(name = "status", nullable = false)
    boolean status;

    @Column(name = "note", columnDefinition = "text", nullable = true)
    String note;

    @ManyToOne
    @JoinColumn(name = "species_id", nullable = false)
    Species species;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToOne(mappedBy = "pet")
    @JsonIgnore
    Prescription prescriptions;
}
