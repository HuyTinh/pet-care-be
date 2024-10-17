package com.pet_care.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.pet_care.identity_service.entity.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest implements Serializable {
    String username;
    @Size(min = 5, message = "Password must be at less 5 characters")
    String password;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
    message = "Email is not valid")
    String email;
    LocalDate dob;
}