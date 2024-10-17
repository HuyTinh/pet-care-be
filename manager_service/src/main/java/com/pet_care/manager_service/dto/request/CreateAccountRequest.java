package com.pet_care.manager_service.dto.request;

import com.pet_care.manager_service.entity.Profile;
import com.pet_care.manager_service.entity.Role;
import com.pet_care.manager_service.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAccountRequest {

    @NotNull(message = "Please Enter Email")
    String email;

    @NotNull(message = "Please Enter Password")
    String password;

    Boolean status = true;

    ProfileRequest profile;

    RoleEnum roleName;
}
