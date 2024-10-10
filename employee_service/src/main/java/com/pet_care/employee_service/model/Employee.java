package com.pet_care.employee_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "employees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("image_url")
    String imageUrl;

    String email;

    String address;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @JsonProperty("account_id")
    Long accountId;

    @JsonProperty("phone_number")
    String phoneNumber;
}
