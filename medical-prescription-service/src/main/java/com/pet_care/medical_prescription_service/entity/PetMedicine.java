package com.pet_care.medical_prescription_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pet_medicines")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetMedicine {
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

    String note;

    @ManyToOne
    @JoinColumn(name = "pet_prescriptions_id")
    PetPrescription petPrescription;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
