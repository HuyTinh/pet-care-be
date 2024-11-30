package com.pet_care.product_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "invoice")
public class Invoice
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String note;

    Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonProperty("create_at")
    @Column(name = "create_at")
    Date createdAt;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<InvoiceDetail> invoiceDetails;

}
