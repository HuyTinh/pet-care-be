package com.pet_care.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Entity(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    String name;
    String description;

    
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdAt;

    
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedAt;
}
