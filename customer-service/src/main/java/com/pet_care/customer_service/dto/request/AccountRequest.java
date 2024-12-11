package com.pet_care.customer_service.dto.request;

// Import necessary Lombok annotations for boilerplate code generation
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO for encapsulating account-related request data.
 * This class is used when dealing with account information in requests.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

    /**
     * Unique identifier for the account.
     */
    Long id;

    /**
     * Email associated with the account.
     */
    String email;
}
