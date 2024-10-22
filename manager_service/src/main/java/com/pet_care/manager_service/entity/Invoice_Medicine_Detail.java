package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="invoice_medicine_details")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice_Medicine_Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "price", nullable = false)
    double price;

    @Column(name = "note", columnDefinition = "text", nullable = false)
    String note;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoice invoice;
}
