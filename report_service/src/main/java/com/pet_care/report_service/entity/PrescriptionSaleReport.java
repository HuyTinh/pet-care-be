package com.pet_care.report_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String name;

    @Temporal(TemporalType.DATE)
    Date createdAt;
}
