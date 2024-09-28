package com.pet_care.identity_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "invalidated_tokens")
public class InvalidatedToken {
    @Id
    String id;
    Date expriryDate;
}
