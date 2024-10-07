package com.pet_care.identity_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet_care.identity_service.enums.Provide;
import com.pet_care.identity_service.model.Account;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Account}
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreateRequest implements Serializable {
    String password;

    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "EMAIL_INVALID")
    String email;

    @JsonProperty("first_name")
    String firstName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Provide provide = Provide.LOCAL;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    @Size(min = 5, message = "PHONE_NUMBER_INVALID")
    String phoneNumber;

    Set<String> roles;
}