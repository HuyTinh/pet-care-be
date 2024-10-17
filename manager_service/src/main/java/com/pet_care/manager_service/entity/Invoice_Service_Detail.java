package com.pet_care.manager_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="invoice_service_details")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice_Service_Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "discount", nullable = false)
    double discount;

    @Column(name = "price", nullable = false)
    double price;

    @Column(name = "note", columnDefinition = "text", nullable = true)
    String note;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    Services services;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoice invoice;
}
