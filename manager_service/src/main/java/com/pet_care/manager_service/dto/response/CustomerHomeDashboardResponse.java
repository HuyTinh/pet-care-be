package com.pet_care.manager_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerHomeDashboardResponse {
    Long countCustomer;

}
