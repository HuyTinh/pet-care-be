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
@Table(name="customers")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "last_name", nullable = false)
    String last_name;

    @Column(name = "first_name", nullable = false)
    String first_name;

    @Column(name = "phone_number", nullable = false)
    String phone_number;

    @Column(name = "status", nullable = false)
    boolean status;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = true)
    Account account;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    Set<Appointment> appointments;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    Set<Pet> pets;
}
