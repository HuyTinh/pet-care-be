package com.pet_care.medical_prescription_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "pet_prescriptions")
public class PetPrescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long petId;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    Prescription prescription;

    String note;

    String diagnosis;

    @OneToMany(mappedBy = "petPrescription", fetch = FetchType.EAGER)
    Set<PrescriptionDetail> medicines;
}
