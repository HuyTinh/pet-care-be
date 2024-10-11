package com.pet_care.medical_prescription_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "prescription_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long petId;

    Long medicineId;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    Prescription prescription;
}
