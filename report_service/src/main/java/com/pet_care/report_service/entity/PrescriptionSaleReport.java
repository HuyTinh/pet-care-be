package com.pet_care.report_service.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "prescription_sale_reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionSaleReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Temporal(TemporalType.DATE)
    Date date;

    Double sale;
}
