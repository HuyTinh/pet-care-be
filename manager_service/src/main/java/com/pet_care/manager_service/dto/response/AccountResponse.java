package com.pet_care.manager_service.dto.response;

import com.pet_care.manager_service.entity.Profile;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Long id;
    String email;
    String password;
    Boolean status;
    Profile profile;
}
