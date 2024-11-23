package com.pet_care.medical_prescription_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
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

    String diagnosis;

    @OneToMany(mappedBy = "petPrescription", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PetMedicine> petMedicines = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;

}
