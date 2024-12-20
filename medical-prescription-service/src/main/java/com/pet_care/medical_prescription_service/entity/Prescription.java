package com.pet_care.medical_prescription_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.medical_prescription_service.enums.PrescriptionStatus;
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
@Entity(name = "prescriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty("appointment_id")
    Long appointmentId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    PrescriptionStatus status = PrescriptionStatus.PENDING_PAYMENT;

    @JsonProperty("total_money")
    Double totalMoney;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
