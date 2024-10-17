package com.pet_care.manager_service.dto.request;

import com.pet_care.manager_service.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountRequest {

    @NotNull(message = "Please Enter Your Email")
    String email;

    @NotNull(message = "Please Enter Your Password")
    String password;

    ProfileRequest profileRequest;

    RoleEnum roleName;
}
