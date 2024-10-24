package com.pet_care.medical_prescription_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

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

    @JsonProperty("medicine_id")
    Long medicineId;

    @JsonProperty("calculation_id")
    Long calculationId;

    Long quantity;

    @JsonProperty("total_money")
    Double totalMoney;

    @ManyToOne
    @JoinColumn(name = "pet_prescriptions_id")
    PetPrescription petPrescription;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
