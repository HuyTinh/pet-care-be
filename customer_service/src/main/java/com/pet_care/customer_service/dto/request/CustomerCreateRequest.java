package com.pet_care.customer_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.customer_service.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreateRequest {
    String email;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;

    @JsonProperty("image_url")
    String imageUrl;

    @JsonProperty("birth_date")
    Date birthDate;

    @JsonProperty("account_id")
    Long accountId;

    public String getImageUrl() {
        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            return "https://api.multiavatar.com/" + this.firstName + this.lastName + ".png";
        }
        return imageUrl;
    }
}
