package com.pet_care.customer_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity(name ="customers" )
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    String phoneNumber;

    String address;

    String email;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @JsonProperty("image_url")
    String imageUrl;

    @JsonProperty("account_id")
    Long accountId;
}
