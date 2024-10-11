package com.pet_care.employee_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.employee_service.enums.Gender;
import com.pet_care.employee_service.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class EmployeeCreateRequest {
    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("image_url")
    String imageUrl;

    String password;

    String email;

    String address;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @JsonProperty("phone_number")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Set<Role> roles;

    public String getImageUrl() {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            return "https://api.multiavatar.com/" + this.firstName + this.lastName + ".png";
        }
        return imageUrl;
    }
}
