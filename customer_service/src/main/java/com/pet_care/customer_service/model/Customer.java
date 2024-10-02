package com.pet_care.customer_service.model;

import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String firstName;

    String lastName;

    String phoneNumber;

    String address;

    String email;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String imageUrl;

    @Temporal(TemporalType.DATE)
    Date birthDate;

    Long accountId;
}
