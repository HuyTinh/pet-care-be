package com.pet_care.manager_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="invoices")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false)
    Date create_date;

    @Column(name = "total", nullable = false)
    double total;

    @Column(name = "note",columnDefinition = "text", nullable = true)
    String note;

    @Column(name = "payment_status", nullable = false)
    boolean payment_status;

    @Column(name = "status", nullable = false)
    boolean status;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    Payment payment;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    Set<Invoice_Medicine_Detail> invoice_medicine_detail;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    Set<Invoice_Service_Detail> invoice_service_detail;
}
