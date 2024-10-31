package com.pet_care.appointment_service.model;

import com.pet_care.appointment_service.enums.HospitalServiceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Hospital_Services")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalServiceEntity {
    @Id
    String name;

    String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    HospitalServiceStatus status = HospitalServiceStatus.ACTIVE;

    Double price;

    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;
}
