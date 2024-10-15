package com.pet_care.bill_service.model;

import com.pet_care.bill_service.enums.InvoiceStatus;
import com.pet_care.bill_service.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "invoices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long prescriptionId;

    Long appointmentId;

    Double prescriptionAmount;

    Double appointmentAmount;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    InvoiceStatus status;

    @Temporal(TemporalType.DATE)
    Date createdAt;

    @Temporal(TemporalType.DATE)
    Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
