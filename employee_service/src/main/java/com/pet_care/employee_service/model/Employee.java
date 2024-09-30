package com.pet_care.employee_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("employees")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    @Id
    Long id;

    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;
    String email;
    @JsonProperty("phone_number")
    String phoneNumber;
    String address;
    Gender gender;

    @JsonProperty("account_id")
    Long accountId;
}
