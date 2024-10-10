package com.pet_care.medical_prescription_service.model;

import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "prescriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long appointmentId;

    @Enumerated(EnumType.STRING)
    PrescriptionStatus status;

    @OneToMany(mappedBy = "prescription", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    Set<PrescriptionDetail> prescriptionDetails;
}
