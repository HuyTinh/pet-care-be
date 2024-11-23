package com.pet_care.medical_prescription_service.model;

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
@Entity(name = "pet_veterinary_care")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetVeterinaryCare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty("veterinary_care")
    String veterinaryCare;

    String result;

    @JsonProperty("total_money")
    Double totalMoney;

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
