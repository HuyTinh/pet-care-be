package com.petcare.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "appointments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    LocalDate appointmentDate;

    LocalTime appointmentTime;

    boolean status;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    Owner owner;
}
