package com.pet_care.manager_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="prescriptions")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "create_date", nullable = false)
    LocalDate create_date;

    @Column(name = "disease_name", nullable = true)
    String disease_name;

    @Column(name = "note", columnDefinition = "text", nullable = true)
    String note;

    @Column(name = "status", nullable = false)
    boolean status = true;

    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false)
    Pet pet;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    Profile profile;
}
