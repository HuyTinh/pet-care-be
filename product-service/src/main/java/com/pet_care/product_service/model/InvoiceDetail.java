package com.pet_care.product_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "invoice-details")
public class InvoiceDetail
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int quantity;

    @JsonProperty("total_price")
    @Column(name = "total_price")
    Double totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonProperty("create_at")
    @Column(name = "create_at")
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @JsonProperty("update_at")
    @Column(name = "update_at")
    Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonIgnore
    Invoice invoice;

}
